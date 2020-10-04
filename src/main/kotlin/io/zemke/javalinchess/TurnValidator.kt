package io.zemke.javalinchess

import io.zemke.javalinchess.controller.Match
import io.zemke.javalinchess.controller.Player
import io.zemke.javalinchess.figure.Figure
import kotlin.jvm.Throws

object TurnValidator {

    @Throws(NotThatPlayersTurnException::class)
    fun assertTurn(match: Match, figure: Figure, gridTarget: Array<Int>) {
        if (figure.player != match.nextTurn) throw NotThatPlayersTurnException(figure.player, match.nextTurn)
        // todo validate the turn
    }

    class NotThatPlayersTurnException(player: Player, nextTurn: Player)
        : RuntimeException("Next turn is $nextTurn but $player given.")
}
