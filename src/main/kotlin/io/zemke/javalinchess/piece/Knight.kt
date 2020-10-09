package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board

class Knight(color: Color, position: Position) : Piece("Knight", color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        TODO("Not yet implemented")
    }
}
