package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board

class Knight(color: Color, position: Position) : Piece("Knight", color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPositionOfPiece(this)
                ?: throw RuntimeException("Position of Piece $this not found")
        val ownPieces = board.ownPieces(this.color).map { it.position }
        return setOf(
                Position(current.file + 2, current.rank - 1),
                Position(current.file + 2, current.rank + 1),
                Position(current.file - 2, current.rank - 1),
                Position(current.file - 2, current.rank + 1),
                Position(current.file - 1, current.rank + 2),
                Position(current.file + 1, current.rank + 2),
                Position(current.file - 1, current.rank - 2),
                Position(current.file + 1, current.rank - 2),
        ).filter { !ownPieces.contains(it) }.toSet()
    }
}
