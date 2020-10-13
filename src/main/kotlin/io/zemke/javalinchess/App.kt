package io.zemke.javalinchess

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.BadRequestResponse
import io.javalin.http.InternalServerErrorResponse
import io.javalin.plugin.json.JavalinJson
import io.zemke.javalinchess.complex.Memcached
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.Match
import io.zemke.javalinchess.chess.piece.Color
import io.zemke.javalinchess.view.model.TurnDto

fun main() {
    val app = Javalin.create().start(7000)
    app.get("/") { ctx -> ctx.result("Hello World") }
    app.routes {
        path("match") {
            post {
                val match = Match(
                        board = Board(true),
                        nextTurn = Color.BLACK
                )
                when (Memcached.store(match.id, match)) {
                    true -> it.json(match)
                    else -> throw InternalServerErrorResponse()
                }
            }
            path(":key") {
                get {
                    it.json(Memcached.retrieve<Match>(it.pathParam("key")))
                }
                path("turn") {
                    post {
                        val match = Memcached.retrieve<Match>(it.pathParam("key"))
                        val turn = JavalinJson.fromJson(it.body(), TurnDto::class.java)
                        val piece = match.board.findPieceById(turn.piece)
                                ?: throw BadRequestResponse("Piece ${turn.piece} not found in grid")
                        if (!piece.allowedNextPositions(match.board).contains(turn.target)) {
                            throw BadRequestResponse("Piece $piece cannot move to ${turn.target}")
                        }
                        match.board.move(piece, turn.target)
                    }
                }
            }
        }
    }
}
