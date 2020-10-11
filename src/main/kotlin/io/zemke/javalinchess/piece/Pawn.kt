package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import kotlin.math.abs

class Pawn(color: Color, position: Position) : Piece("Pawn", color, position) {

    // todo promotion
    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPositionOfPiece(this)
        val fn = if (color == Color.BLACK) Int::inc else Int::dec
        val allowedNextPositions =
                if (longLeapAllowed()) {
                    mutableSetOf(
                            Position(current.file, fn(current.rank)),
                            Position(current.file, fn(fn(current.rank))))
                } else {
                    mutableSetOf(Position(current.file, fn(current.rank)))
                }
        if (board.getPieceAt(Position(current.file + 1, fn(current.rank)))?.color == color.other()) {
            allowedNextPositions.add(Position(current.file + 1, fn(current.rank)))
        }
        if (board.getPieceAt(Position(current.file - 1, fn(current.rank)))?.color == color.other()) {
            allowedNextPositions.add(Position(current.file - 1, fn(current.rank)))
        }
        if (board.isPassible(Position(current.file + 1, current.rank))) {
            allowedNextPositions.add(Position(current.file + 1, fn(current.rank)))
        }
        if (board.isPassible(Position(current.file - 1, current.rank))) {
            allowedNextPositions.add(Position(current.file - 1, fn(current.rank)))
        }
        return allowedNextPositions
    }

    fun isLongLeap(from: Position, to: Position): Boolean =
            from.file == to.file && abs(from.rank - to.rank) == 2

    private fun longLeapAllowed(): Boolean =
            (color == Color.BLACK && position.rank == 1)
                    || (color == Color.WHITE && position.rank == 6)
}
