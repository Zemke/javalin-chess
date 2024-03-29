package io.zemke.javalinchess.view.controller

import io.javalin.http.Context
import io.zemke.javalinchess.DelegationContext
import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.filterNotInCheck
import io.zemke.javalinchess.chess.piece.King
import io.zemke.javalinchess.complex.Memcached

@Zemke
class PieceController {

    @Inject
    private lateinit var memcached: Memcached

    fun allowedNextPositions(_ctx: Context) {
        val ctx = DelegationContext(_ctx)
        val pieceId = ctx.pathParam("pieceKey")
        val board = memcached.retrieve<Board>(ctx.pathParam("key"))
        val piece = board.findPieceById(pieceId) ?: throw RuntimeException()
        val ownKing = board.ownPieces(board.nextTurn).find { it is King }!! as King
        ctx.status(200)
        var allowedNextPositions = piece.allowedNextPositions(board).filterNotInCheck(board, piece)
        if (ownKing.inCheck(board) && piece !is King) {
            allowedNextPositions = allowedNextPositions
                    .filter { !ownKing.inCheck(Board(board).move(piece, it)) }
                    .toSet()
        }
        ctx.json(allowedNextPositions)
    }
}
