package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board

class Pawn(color: Color, position: Position) : Piece("Pawn", color, position) {
    override fun allowedNextPositions(board: Board): Set<Position> {
        TODO("Not yet implemented")
    }
}
