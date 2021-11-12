package io.zemke.javalinchess

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.staticfiles.Location
import io.javalin.plugin.rendering.vue.JavalinVue
import io.javalin.plugin.rendering.vue.VueComponent
import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import io.zemke.javalinchess.view.controller.BoardController
import io.zemke.javalinchess.view.controller.CastleController
import io.zemke.javalinchess.view.controller.PieceController
import io.zemke.javalinchess.view.controller.TurnController

@Zemke
class JavalinChess {

    @Inject
    private lateinit var boardController: BoardController

    @Inject
    private lateinit var turnController: TurnController

    @Inject
    private lateinit var pieceController: PieceController

    @Inject
    private lateinit var castleController: CastleController

    fun run() {
        if (this::class.java.getResource("/vue").toURI().scheme == "jar") {
            JavalinVue.rootDirectory("/vue", Location.CLASSPATH)
        }
        val app = Javalin.create { config ->
            config.enableWebjars()
            config.addStaticFiles("/public")
        }.start(7000)
        app.get("/", VueComponent("<board></board>"))
        // TODO "spinoff" flag that when given instantiates a new board rather than mutating the existing
        app.routes {
            path("api") {
                get("/") { ctx -> ctx.result("Hello World") }
                path("board") {
                    get("", boardController::get)
                    post(boardController::create)
                    path(":key") {
                        get(boardController::get)
                        get("turns", boardController::turns)
                        get("piece/:pieceKey/allowed-next-positions", pieceController::allowedNextPositions)
                        path("turn") {
                            post(turnController::create)
                        }
                        post("castle/:side", castleController::castle)
                    }
                }
            }
        }
    }
}
