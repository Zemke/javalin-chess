package io.zemke.javalinchess.figure

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class King(player: Player, color: Color, position: Position) : Figure("King", player, color, position) {
    override fun move(board: Board, target: Position): Board {
        TODO("Not yet implemented")
    }
}
