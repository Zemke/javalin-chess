package io.zemke.javalinchess.view.controller

import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJson
import io.zemke.javalinchess.DelegationContext
import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.filterNotInCheck
import io.zemke.javalinchess.chess.piece.Position
import io.zemke.javalinchess.complex.Memcached
import io.zemke.javalinchess.view.model.PieceTargetsDto

@Zemke
class BoardController {

    @Inject
    private lateinit var memcached: Memcached

    fun create(_ctx: Context) {
        val ctx = DelegationContext(_ctx)
        val board = Board(true)
        board.uuidWhite = ctx.header("auth")
        memcached.store(board.id, board)
        ctx.json(board)
        ctx.status(201)
    }

    fun turns(_ctx: Context) = DelegationContext(_ctx).let { ctx ->
        memcached.retrieve<Board>(ctx.pathParam("key")).also { board ->
            board.ownPieces(board.nextTurn)
                .map { PieceTargetsDto(it, it.allowedNextPositions(board).filterNotInCheck(board, it)) }
                .also { ctx.json(it) }
            ctx.status(200)
        }
    }

    fun get(_ctx: Context) = DelegationContext(_ctx).let { ctx ->
        memcached.retrieve<Board>(ctx.pathParam("key"))?.let { ctx.json(it) }
                ?: with(ctx) { result("no board ${ctx.pathParam("key")}"); status(404) }
    }
}
