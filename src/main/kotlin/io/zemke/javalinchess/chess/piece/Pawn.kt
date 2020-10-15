package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.Color.BLACK
import io.zemke.javalinchess.chess.piece.Color.WHITE
import kotlin.math.abs

class Pawn(color: Color, position: Position) : Piece("Pawn", color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPositionOfPiece(this)
        val fn = if (color == BLACK) Int::inc else Int::dec
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

    fun <T : Piece> promote(board: Board, pieceProducer: (Color, Position) -> T): T {
        val position = board.findPositionOfPiece(this)
        val piece = pieceProducer(color, position)
        board.putPiece(piece, true)
        return piece
    }

    fun promotable(board: Board): Boolean {
        val (_, rank) = board.findPositionOfPiece(this)
        return ((color == BLACK && rank == 7)
                || (color == WHITE && rank == 0))
    }

    private fun longLeapAllowed(): Boolean =
            (color == BLACK && position.rank == 1)
                    || (color == WHITE && position.rank == 6)
}