package io.zemke.javalinchess

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.zemke.javalinchess.view.controller.BoardController

fun main() {
    val app = Javalin.create().start(7000)
    app.get("/") { ctx -> ctx.result("Hello World") }
    app.routes {
        path("board") {
            post(BoardController::create)
            path(":key") {
                get(BoardController::get)
                path("turn") {
                    post(BoardController.TurnController::create)
                }
            }
        }
    }
}
