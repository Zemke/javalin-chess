package io.zemke.javalinchess

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.BadRequestResponse
import io.javalin.http.InternalServerErrorResponse
import io.javalin.plugin.json.JavalinJson
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.Color
import io.zemke.javalinchess.chess.piece.Color.BLACK
import io.zemke.javalinchess.complex.Memcached
import io.zemke.javalinchess.view.model.TurnDto

fun main() {
    val app = Javalin.create().start(7000)
    app.get("/") { ctx -> ctx.result("Hello World") }
    app.routes {
        path("match") {
            post {
                val board = Board(true)
                board.uuidWhite = it.header("auth")
                when (Memcached.store(board.id, board)) {
                    true -> it.json(board)
                    else -> throw InternalServerErrorResponse()
                }
            }
            path(":key") {
                get {
                    it.json(Memcached.retrieve<Board>(it.pathParam("key")))
                }
                path("turn") {
                    post {
                        val board = Memcached.retrieve<Board>(it.pathParam("key"))
                        val turn = JavalinJson.fromJson(it.body(), TurnDto::class.java)
                        val piece = board.findPieceById(turn.piece)
                                ?: throw BadRequestResponse("Piece ${turn.piece} not found in grid")
                        if (board.nextTurn != piece.color) {
                            throw BadRequestResponse("It's not ${piece.color}'s turn.")
                        }
                        if (!piece.allowedNextPositions(board).contains(turn.target)) {
                            throw BadRequestResponse("Piece $piece cannot move to ${turn.target}")
                        }
                        val auth = it.header("auth")
                        if (piece.color == BLACK && board.uuidBlack != null && auth != board.uuidBlack) {
                            throw BadRequestResponse(
                                    "Black requested with $auth but requested with ${board.uuidBlack}.")
                        }
                        if (piece.color == Color.WHITE && board.uuidWhite != null && auth != board.uuidWhite) {
                            throw BadRequestResponse(
                                    "White requested with $auth but requested with ${board.uuidWhite}.")
                        }
                        if (piece.color == BLACK && board.uuidBlack == null) {
                            board.uuidBlack = auth
                        }
                        board.move(piece, turn.target)
                        board.nextTurn()
                    }
                }
            }
        }
    }
}
