package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class Bishop(player: Player, color: Color, position: Position) : Piece("Bishop", player, color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPositionOfPiece(this)
                ?: throw IllegalArgumentException("Piece $this is not on board.")
        return setOf(
                *addPositions(current, Int::inc, Int::inc),
                *addPositions(current, Int::dec, Int::dec),
                *addPositions(current, Int::inc, Int::dec),
                *addPositions(current, Int::dec, Int::inc))
    }

    private fun addPositions(current: Position,
                             fileModifierFn: (Int) -> Int, rankModifierFn: (Int) -> Int): Array<Position> {
        var file = current.file
        var rank = current.rank
        val positions = mutableSetOf<Position>()
        for (i in 1..7) {
            file = fileModifierFn(file)
            rank = rankModifierFn(rank)
            if (!Position.isValidPosition(file, rank)) break
            positions.add(Position(file, rank))
        }
        return positions.toTypedArray()
    }
}
