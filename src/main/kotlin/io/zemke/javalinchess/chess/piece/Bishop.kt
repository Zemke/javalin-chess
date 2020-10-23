package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board

class Bishop(color: Color, position: Position) : Piece("Bishop", color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPosition(this)
        return setOf(
                *addPositions(board, current, Int::inc, Int::inc),
                *addPositions(board, current, Int::dec, Int::dec),
                *addPositions(board, current, Int::inc, Int::dec),
                *addPositions(board, current, Int::dec, Int::inc))
    }

    private fun addPositions(board: Board, current: Position,
                             fileModifierFn: (Int) -> Int, rankModifierFn: (Int) -> Int): Array<Position> {
        var file = current.file
        var rank = current.rank
        val positions = mutableSetOf<Position>()
        for (i in 1..7) {

            file = fileModifierFn(file)
            rank = rankModifierFn(rank)
            if (!Position.isValidPosition(file, rank)) break
            val nextPosition = Position(file, rank)
            val pieceAt = board.findPiece(nextPosition)
            if (pieceAt != null) {
                if (pieceAt.isOwn(this.color)) {
                    break
                } else {
                    positions.add(nextPosition)
                    break
                }
            } else {
                positions.add(nextPosition)
            }
        }
        return positions.toTypedArray()
    }
}
