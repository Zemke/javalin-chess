package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.Color.BLACK
import io.zemke.javalinchess.chess.piece.Color.WHITE

class Rook(color: Color, position: Position) : Piece("Rook", color, position) {

    enum class Side { QUEENSIDE, KINGSIDE }

    val side: Side?

    init {
        val queensidePosition = when (color) {
            WHITE -> Position(0, 7); BLACK -> Position(0, 0)
        }
        val kingsidePosition = when (color) {
            WHITE -> Position(7, 7); BLACK -> Position(7, 0)
        }
        side = when (position) {
            queensidePosition -> Side.QUEENSIDE
            kingsidePosition -> Side.KINGSIDE
            else -> null
        }
    }

    override fun allowedNextPositions(board: Board): Set<Position> {
        val current = board.findPositionOfPiece(this)
        val allowedOnEmptyBoard = generallyAllowedForPiece(current)
        val result = mutableSetOf(*allowedOnEmptyBoard.toTypedArray())

        // same rank
        removeProhibitedPositions(result, current, board, (current.file + 1)..7, true) // files to the right
        removeProhibitedPositions(result, current, board, (current.file - 1 downTo 0), true) // files to the left

        // same file
        removeProhibitedPositions(result, current, board, (current.rank + 1)..7, false) // ranks upward
        removeProhibitedPositions(result, current, board, (current.rank - 1 downTo 0), false) // ranks downward

        return result
    }

    private fun removeProhibitedPositions(
            result: MutableSet<Position>,
            current: Position,
            board: Board,
            progression: IntProgression,
            checkOnRank: Boolean) {
        val ascending = progression.last > progression.first
        for (fileOrRank in progression) {
            val subjectPosition = when (checkOnRank) {
                true -> Position(fileOrRank, current.rank)
                false -> Position(current.file, fileOrRank)
            }
            val pieceAtPos = board.getPieceAt(subjectPosition)
            if (pieceAtPos != null) {
                if (pieceAtPos.isOwn(color)) {
                    val progressionToRemove = IntProgression.fromClosedRange(
                            fileOrRank, progression.last, if (ascending) +1 else -1)
                    when (checkOnRank) {
                        true -> result.removeIf { it.rank == current.rank && progressionToRemove.contains(it.file) }
                        false -> result.removeIf { it.file == current.file && progressionToRemove.contains(it.rank) }
                    }
                } else {
                    val progressionToRemove = IntProgression.fromClosedRange(
                            fileOrRank + (if (ascending) +1 else -1), progression.last, if (ascending) +1 else -1)
                    when (checkOnRank) {
                        true -> result.removeIf { it.rank == current.rank && progressionToRemove.contains(it.file) }
                        false -> result.removeIf { it.file == current.file && progressionToRemove.contains(it.rank) }
                    }
                }
                break
            }
        }
    }

    private fun generallyAllowedForPiece(current: Position): Set<Position> {
        val allowedOnSameRank = mutableSetOf<Position>()
        for (num in 0 until current.file) allowedOnSameRank.add(Position(num, current.rank))
        for (num in current.file..7) allowedOnSameRank.add(Position(num, current.rank))
        val allowedOnSameFile = mutableSetOf<Position>()
        for (num in 0 until current.rank) allowedOnSameFile.add(Position(current.file, num))
        for (num in current.rank..7) allowedOnSameFile.add(Position(current.file, num))
        return setOf(*allowedOnSameRank.toTypedArray(), *allowedOnSameFile.toTypedArray())
                .filter { it != current }.toSet()
    }
}
