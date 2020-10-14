package io.zemke.javalinchess

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import io.zemke.javalinchess.view.controller.BoardController
import io.zemke.javalinchess.view.controller.TurnController

fun main() {
    JavalinChess().run()
}

class JavalinChess @Zemke constructor() {

    @Inject
    private lateinit var boardController: BoardController

    @Inject
    private lateinit var turnController: TurnController

    fun run() {
        val app = Javalin.create().start(7000)
        app.get("/") { ctx -> ctx.result("Hello World") }
        app.routes {
            path("board") {
                post(boardController::create)
                path(":key") {
                    get(boardController::get)
                    path("turn") {
                        post(turnController::create)
                    }
                }
            }
        }
    }
}
