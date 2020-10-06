package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class King(player: Player, color: Color, position: Position) : Piece("King", player, color, position) {
    override fun allowedNextPositions(board: Board): List<Position> {
        TODO("Not yet implemented")
    }
}
