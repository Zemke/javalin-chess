package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board

class King(color: Color, position: Position) : Piece("King", color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPositionOfPiece(this)
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
        )
                .filter { !ownPieces.contains(it) }
                .filter { !inCheck(Board(board).move(this, it)) }
                .toSet()
    }

    fun castlingAllowed(board: Board, rook: Rook): Boolean {
        val positionsBetween =
                if (position.file > rook.position.file) rook.position.file - 1 downTo position.file + 1
                else position.file + 1 downTo rook.position.file - 1
        return positionsBetween.none { board.getPieceAt(Position(position.rank, it)) != null }
                && board.findMovements(rook).isEmpty() && board.findMovements(this).isEmpty()
        // TODO One may not castle out of, through, or into check.
    }

    fun castle(board: Board, rook: Rook) {
        val rookPosition = board.findPositionOfPiece(rook)
        val (file, rank) = board.findPositionOfPiece(this)
        when (rookPosition.file) {
            7 -> {
                board.move(rook, Position(file + 1, rank))
                board.move(this, Position(file + 2, rank))
            }
            0 -> {
                board.move(rook, Position(file - 1, rank))
                board.move(this, Position(file - 2, rank))
            }
            else -> {
                throw InvalidCastlingException("Rooks are not in a position to perform castling.")
            }
        }
    }

    fun inCheck(board: Board): Boolean {
        return board.opponentPieces(color)
                .map { it.allowedNextPositions(board) }
                .flatten()
                .contains(board.findPositionOfPiece(this))
    }

    fun checkmated(board: Board): Boolean {
        return allowedNextPositions(board)
                .all { inCheck(Board(board).move(this@King, it)) }
    }

    class InvalidCastlingException(message: String) : RuntimeException(message)
}

