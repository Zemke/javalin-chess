package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.Color.BLACK
import io.zemke.javalinchess.chess.piece.Color.WHITE
import kotlin.math.abs

class Pawn(color: Color, position: Position) : Piece("Pawn", color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        return allowedNextPositions(board, false)
    }

    fun allowedNextPositions(board: Board, onlyCapturing: Boolean): Set<Position> {
        val current = board.findPosition(this)
        val fn = if (color == BLACK) Int::inc else Int::dec
        val allowedNextPositions = mutableSetOf<Position>()
        if (!onlyCapturing) {
            var position = Position.ifValid(current.file, fn(current.rank))
            if (position != null && board.findPiece(position) == null) {
                allowedNextPositions.add(position)
                if (longLeapAllowed(board)) {
                    position = Position.ifValid(current.file, fn(fn(current.rank)))
                    if (position != null && board.findPiece(position) == null) {
                        allowedNextPositions.add(position)
                    }
                }
            }
        }
        var checkPosition: Position?
        checkPosition = Position.ifValid(current.file + 1, fn(current.rank))
        if (checkPosition != null && board.findPiece(checkPosition)?.color == color.other()) {
            allowedNextPositions.add(Position(current.file + 1, fn(current.rank)))
        }
        checkPosition = Position.ifValid(current.file - 1, fn(current.rank))
        if (checkPosition != null && board.findPiece(checkPosition)?.color == color.other()) {
            allowedNextPositions.add(Position(current.file - 1, fn(current.rank)))
        }
        checkPosition = Position.ifValid(current.file + 1, current.rank)
        if (checkPosition != null && board.isPassible(checkPosition)) {
            allowedNextPositions.add(Position(current.file + 1, fn(current.rank)))
        }
        checkPosition = Position.ifValid(current.file - 1, current.rank)
        if (checkPosition != null && board.isPassible(checkPosition)) {
            allowedNextPositions.add(Position(current.file - 1, fn(current.rank)))
        }
        return allowedNextPositions
    }

    fun isLongLeap(from: Position, to: Position): Boolean =
            from.file == to.file && abs(from.rank - to.rank) == 2

    fun <T : Piece> promote(board: Board, pieceProducer: (Color, Position) -> T): T {
        val position = board.findPosition(this)
        val piece = pieceProducer(color, position)
        board.putPiece(piece, true)
        return piece
    }

    fun promotable(board: Board): Boolean {
        val (_, rank) = board.findPosition(this)
        return ((color == BLACK && rank == 7)
                || (color == WHITE && rank == 0))
    }

    private fun longLeapAllowed(board: Board): Boolean {
        val current = board.findPosition(this)
        return ((color == BLACK && current.rank == 1)
                || (color == WHITE && current.rank == 6))
    }
}

