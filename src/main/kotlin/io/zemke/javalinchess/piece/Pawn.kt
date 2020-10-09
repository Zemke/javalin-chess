package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class Pawn(player: Player, color: Color, position: Position) : Piece("Pawn", player, color, position) {
    override fun allowedNextPositions(board: Board): Set<Position> {
        TODO("Not yet implemented")
    }
}
