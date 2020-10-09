package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board

class Queen(color: Color, position: Position) : Piece("Queen", color, position) {
    override fun allowedNextPositions(board: Board): Set<Position> {
        TODO("Not yet implemented")
    }
}
