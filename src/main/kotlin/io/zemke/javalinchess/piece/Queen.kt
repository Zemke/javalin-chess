package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class Queen(player: Player, color: Color, position: Position) : Piece("Queen", player, color, position) {
    override fun allowedNextPositions(board: Board): List<Position> {
        TODO("Not yet implemented")
    }
}
