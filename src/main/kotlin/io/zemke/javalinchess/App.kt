package io.zemke.javalinchess

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.rendering.vue.VueComponent
import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import io.zemke.javalinchess.view.controller.BoardController
import io.zemke.javalinchess.view.controller.TurnController

fun main() {
    JavalinChess().run()
}

@Zemke
class JavalinChess {

    @Inject
    private lateinit var boardController: BoardController

    @Inject
    private lateinit var turnController: TurnController

    fun run() {
        val app = Javalin.create { config ->
            config.enableWebjars()
            config.addStaticFiles("/public")
        }.start(7000)
        app.get("/", VueComponent("<board></board>"))
        app.routes {
            path("api") {
                get("/") { ctx -> ctx.result("Hello World") }
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
}
