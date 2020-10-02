package io.zemke.javalinchess.controller

import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJson
import io.zemke.javalinchess.chess.Chess
import io.zemke.javalinchess.complex.Memcached
import io.zemke.javalinchess.figure.Figure

object MatchController {

    fun create(ctx: Context) {
        println(Chess.starting)
        println(JavalinJson.toJson(Chess.starting))
        Memcached.store("match", JavalinJson.toJson(Chess.starting))
    }

    fun read(ctx: Context) {
        ctx.json(Memcached.retrieve<List<List<Figure>>>("match"))
    }
}
