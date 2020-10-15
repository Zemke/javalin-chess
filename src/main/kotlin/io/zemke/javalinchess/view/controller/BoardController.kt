package io.zemke.javalinchess.view.controller

import io.javalin.http.Context
import io.javalin.http.InternalServerErrorResponse
import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.complex.Memcached

@Zemke
class BoardController {

    @Inject
    private lateinit var memcached: Memcached

    fun create(ctx: Context) {
        val board = Board(true)
        board.uuidWhite = ctx.header("auth")
        when (memcached.store(board.id, board)) {
            true -> ctx.json(board)
            else -> throw InternalServerErrorResponse()
        }
        ctx.status(201)
    }

    fun get(ctx: Context) {
        ctx.json(memcached.retrieve<Board>(ctx.pathParam("key")))
    }
}
