package io.zemke.javalinchess

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.BadRequestResponse
import io.javalin.http.InternalServerErrorResponse
import io.javalin.plugin.json.JavalinJson
import io.zemke.javalinchess.complex.Memcached
import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Match
import io.zemke.javalinchess.controller.Player

fun main() {
    val app = Javalin.create().start(7000)
    app.get("/") { ctx -> ctx.result("Hello World") }
    app.routes {
        path("match") {
            post {
                val player1 = Player("John")
                val player2 = Player("Ronny")
                val match = Match(
                        board = Board(player1, player2),
                        player1 = player1,
                        player2 = player2,
                        nextTurn = player1
                )
                when (Memcached.store(match.id, match)) {
                    true -> it.json(match)
                    else -> throw InternalServerErrorResponse()
                }
            }
            path(":key") {
                get {
                    it.json(Memcached.retrieve<Match>(it.pathParam("key")))
                }
                path("turn") {
                    post {
                        val match = Memcached.retrieve<Match>(it.pathParam("key"))
                        val turn = JavalinJson.fromJson(it.body(), TurnDto::class.java)
                        val figure = match.board.findFigureById(turn.figure)
                                ?: throw BadRequestResponse("Figure ${turn.figure} not found in grid")
                        TurnValidator.assertTurn(match, figure, turn.target)
                    }
                }
            }
        }
    }
}
