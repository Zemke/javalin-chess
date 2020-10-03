package io.zemke.javalinchess

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.zemke.javalinchess.chess.Chess
import io.zemke.javalinchess.complex.Memcached
import io.zemke.javalinchess.controller.Match
import io.zemke.javalinchess.controller.Player

fun main() {
    val app = Javalin.create().start(7000)
    app.get("/") { ctx -> ctx.result("Hello World") }
    app.routes {
        path("match") {
            post {
                val player1 = Player("John")
                val match = Match(
                        grid = Chess.starting,
                        player1 = player1,
                        player2 = Player("Ronny"),
                        nextTurn = player1
                )
                Memcached.store("match", match)
            }
            get {
                it.json(Memcached.retrieve<String>("match"))
            }
        }
    }
}
