package io.zemke.javalinchess

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.zemke.javalinchess.controller.MatchController

fun main() {
    val app = Javalin.create().start(7000)
    app.get("/") { ctx -> ctx.result("Hello World") }
    app.routes {
        path("match") {
            post(MatchController::create)
            get(MatchController::read)
        }
    }
}
