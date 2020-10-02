package io.zemke.javalinchess.controller

import io.javalin.http.Context
import io.zemke.javalinchess.complex.Memcached

object MatchController {

    fun create(ctx: Context) {
        Memcached.store("something", "hydraulic")
    }

    fun read(ctx: Context) {
        ctx.result(Memcached.retrieve<String>("something"))
    }

    fun test(ctx: Context) {
        ctx.result("worx")
    }
}
