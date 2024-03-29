package io.zemke.javalinchess.view.controller

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJson
import io.zemke.javalinchess.DelegationContext
import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.*
import io.zemke.javalinchess.complex.Memcached
import io.zemke.javalinchess.view.ViewUtil
import io.zemke.javalinchess.view.model.TurnDto

@Zemke
class TurnController {

    @Inject
    private lateinit var memcached: Memcached

    fun create(_ctx: Context) {
        val ctx = DelegationContext(_ctx)
        val spinoff = ctx.queryParam("spinoff").let { it == "1" || it == "true" }
        val board = memcached.retrieve<Board>(ctx.pathParam("key"))
            .let { b -> if (spinoff) Board(b) else b }
        val turn = JavalinJson.fromJson(ctx.body(), TurnDto::class.java)
        val piece = board.findPieceById(turn.piece)
                ?: throw BadRequestResponse("Piece ${turn.piece} not found in grid")
        if (board.nextTurn != piece.color) {
            throw BadRequestResponse("It's not ${piece.color}'s turn.")
        }
        if (!piece.allowedNextPositions(board).contains(turn.target)) {
            throw BadRequestResponse("Piece $piece cannot move to ${turn.target}")
        }
        Board(board).let { b ->
            if (b.move(piece, turn.target).findPiece<King>(board.nextTurn)?.inCheck(b) ?: false) {
                throw BadRequestResponse("Cannot move into check.")
            }
        }
        val auth = ctx.header("auth")
        if (!spinoff && !ViewUtil.isAuth(board, auth)) {
            throw BadRequestResponse(
                    "${board.nextTurn} UUID expected " +
                            "${if (board.nextTurn == Color.BLACK) board.uuidBlack else board.uuidWhite} " +
                            "but given is $auth.")
        }
        if (board.nextTurn == Color.BLACK && board.uuidBlack == null) {
            board.uuidBlack = auth
        }
        board.move(piece, turn.target)
        if (piece.promotable(board)) {
            piece.promote(board) { color, position ->
                when (turn.promotion?.let { it.toLowerCase() }) {
                    "bishop" -> Bishop(color, position)
                    "knight" -> Knight(color, position)
                    "rook" -> Rook(color, position)
                    "queen" -> Queen(color, position)
                    else -> throw BadRequestResponse(
                            "Valid promotion targets are Bishop, Knight, Rook, Queen.")
                }
            }
        }
        board.nextTurn()
        memcached.store(board.id, board)
        ctx.status(201)
        ctx.json(board)
    }
}
