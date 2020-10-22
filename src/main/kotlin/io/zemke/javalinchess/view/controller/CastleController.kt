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

    fun castle(_ctx: Context) {
        val ctx = DelegationContext(_ctx)
        val side = Rook.Side.valueOf(ctx.pathParam("side").toUpperCase())
        val pieceId = ctx.pathParam("pieceKey")
        val board = memcached.retrieve<Board>(ctx.pathParam("key"))
        val king = board.findPieceById(pieceId) ?: throw RuntimeException()
        if (king !is King) throw BadRequestResponse("Only King can perform castling")
        if (!board.castlingAllowed.contains(side))
            throw BadRequestResponse("Castling is not allowed at the moment")
        board.getRook(side)?.let { rook -> king.castle(board, rook) }
                ?: throw BadRequestResponse("No $side rook.")
        ctx.status(200)
        ctx.json(board)
    }
}
