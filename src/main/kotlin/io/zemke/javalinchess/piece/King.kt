package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class King(player: Player, color: Color, position: Position) : Piece("King", player, color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPositionOfPiece(this)
                ?: throw IllegalArgumentException("Pice $this is not on board.")
        val ownPieces = board.ownPieces(this.color).map { it.position }
        return setOf(
                Position(current.file + 1, current.rank),
                Position(current.file - 1, current.rank),
                Position(current.file, current.rank + 1),
                Position(current.file, current.rank - 1),
                Position(current.file - 1, current.rank - 1),
                Position(current.file - 1, current.rank + 1),
                Position(current.file + 1, current.rank - 1),
                Position(current.file + 1, current.rank + 1),
        ).filter { !ownPieces.contains(it) }.toSet()
    }
}
