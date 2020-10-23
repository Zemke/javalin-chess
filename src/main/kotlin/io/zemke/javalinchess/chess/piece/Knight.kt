package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board

class Knight(color: Color, position: Position) : Piece("Knight", color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPosition(this)
        val ownPieces = board.ownPieces(this.color)
                .mapNotNull { board.findPieceById(it.id) }
                .map { board.findPosition(it) }
        return setOf(
                Position.ifValid(current.file + 2, current.rank - 1),
                Position.ifValid(current.file + 2, current.rank + 1),
                Position.ifValid(current.file - 2, current.rank - 1),
                Position.ifValid(current.file - 2, current.rank + 1),
                Position.ifValid(current.file - 1, current.rank + 2),
                Position.ifValid(current.file + 1, current.rank + 2),
                Position.ifValid(current.file - 1, current.rank - 2),
                Position.ifValid(current.file + 1, current.rank - 2),
        )
                .filterNotNull()
                .filter { !ownPieces.contains(it) }.toSet()
    }
}
