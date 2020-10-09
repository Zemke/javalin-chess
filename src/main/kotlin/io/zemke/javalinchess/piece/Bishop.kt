package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class Bishop(player: Player, color: Color, position: Position) : Piece("Bishop", player, color, position) {
    override fun allowedNextPositions(board: Board): Set<Position> {
        TODO("Not yet implemented")
    }
}
