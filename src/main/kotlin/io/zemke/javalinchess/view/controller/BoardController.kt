package io.zemke.javalinchess.view.controller

import io.javalin.http.Context
import io.zemke.javalinchess.DelegationContext
import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.complex.Memcached

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

    fun get(ctx: Context) {
        ctx.json(memcached.retrieve<Board>(ctx.pathParam("key")))
    }
}
