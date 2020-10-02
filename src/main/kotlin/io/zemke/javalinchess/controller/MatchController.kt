package io.zemke.javalinchess.controller

import io.javalin.http.Context
import io.zemke.javalinchess.chess.Chess
import io.zemke.javalinchess.complex.Memcached

object MatchController {

    fun create(ctx: Context) {
        val player1 = Player("John")
        val match = Match(
                grid = Chess.starting,
                player1 = player1,
                player2 = Player("Ronny"),
                nextTurn = player1
        )
        Memcached.store("match", match)
    }

    fun read(ctx: Context) {
        ctx.json(Memcached.retrieve<String>("match"))
    }
}
