package io.zemke.javalinchess.view.controller

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.zemke.javalinchess.DelegationContext
import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.King
import io.zemke.javalinchess.chess.piece.Rook
import io.zemke.javalinchess.complex.Memcached

@Zemke
class CastleController {

    @Inject
    private lateinit var memcached: Memcached

    fun castle(_ctx: Context) = DelegationContext(_ctx).let { ctx ->
        // TODO auth check
        val side = Rook.Side.valueOf(ctx.pathParam("side").toUpperCase())
        val spinoff = ctx.queryParam("spinoff").let { it == "1" || it == "true" }
        val board = memcached.retrieve<Board>(ctx.pathParam("key"))
            .let { b -> if (spinoff) Board(b) else b }
        val king = board.findPiece<King>(board.nextTurn)
        if (king !is King) throw BadRequestResponse("Only King can perform castling")
        if (!board.castlingAllowed[board.nextTurn]!!.contains(side))
            throw BadRequestResponse("Castling $side is not allowed at the moment")
        board.findRook(side)?.let { rook -> king.castle(board, rook) }
                ?: throw BadRequestResponse("No $side rook.")
        board.nextTurn()
        memcached.store(board.id, board)
        ctx.status(201)
        ctx.json(board)
    }
}
