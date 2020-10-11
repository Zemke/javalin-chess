package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board

class Pawn(color: Color, position: Position) : Piece("Pawn", color, position) {

    // todo en passant
    // todo promotion
    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPositionOfPiece(this)
                ?: throw RuntimeException("Position of Piece $this not found")
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
        return allowedNextPositions
    }

    private fun longLeapAllowed(): Boolean =
            (color == Color.BLACK && position.rank == 1)
                    || (color == Color.WHITE && position.rank == 6)
}
