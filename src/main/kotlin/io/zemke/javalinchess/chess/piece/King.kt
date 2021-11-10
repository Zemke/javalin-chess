package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board

class King(color: Color, position: Position) : Piece("King", color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        return allowedNextPositionsWithoutAllowMoveIntoCheck(board)
                .filter { !inCheck(Board(board).move(this, it)) }
                .toSet()
    }

    /**
     * Like [allowedNextPositions] but without filtering positions which are in check.
     */
    fun allowedNextPositionsWithoutAllowMoveIntoCheck(board: Board): Set<Position> {
        val current = board.findPosition(this)
        val ownPieces = board.ownPieces(this.color)
                .mapNotNull { board.findPieceById(it.id) }
                .map { board.findPosition(it) }
        return setOf(
                Position.ifValid(current.file + 1, current.rank),
                Position.ifValid(current.file - 1, current.rank),
                Position.ifValid(current.file, current.rank + 1),
                Position.ifValid(current.file, current.rank - 1),
                Position.ifValid(current.file - 1, current.rank - 1),
                Position.ifValid(current.file - 1, current.rank + 1),
                Position.ifValid(current.file + 1, current.rank - 1),
                Position.ifValid(current.file + 1, current.rank + 1),
        )
                .filterNotNull()
                .filter { !ownPieces.contains(it) }
                .toSet()
    }

    fun castlingAllowed(board: Board, rook: Rook): Boolean {
        val queenside = position.file > rook.position.file
        val kingPositions: List<Position>
        val rookPositions: List<Position>
        if (queenside) {
            rookPositions = (1..3).map { Position(it, position.rank) }
            kingPositions = (3 downTo 2).map { Position(it, position.rank) }
        } else {
            rookPositions = (6 downTo 5).map { Position(it, position.rank) }
            kingPositions = (5..6).map { Position(it, position.rank) }
        }
        return rookPositions.none { board.findPiece(it) != null }
                && kingPositions.none { board.findPiece(it) != null }
                && kingPositions.none { inCheck(Board(board).move(this, it)) }
                && board.findMovements(rook).isEmpty() && board.findMovements(this).isEmpty()
                && !inCheck(board)
    }

    fun castle(board: Board, rook: Rook) {
        val rookPosition = board.findPosition(rook)
        val (file, rank) = board.findPosition(this)
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
                .map {
                    when (it) {
                        is King -> it.allowedNextPositionsWithoutAllowMoveIntoCheck(board)
                        is Pawn -> it.allowedNextPositions(board, true)
                        else -> it.allowedNextPositions(board)
                    }
                }
                .flatten()
                .contains(board.findPosition(this))
    }

    fun checkmated(board: Board): Boolean {
        // TODO check can be escaped by capturing the attacking piece
        return allowedNextPositions(board)
                .all { inCheck(Board(board).move(this@King, it)) }
    }

    class InvalidCastlingException(message: String) : RuntimeException(message)
}

