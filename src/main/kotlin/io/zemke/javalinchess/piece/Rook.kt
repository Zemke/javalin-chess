package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class Rook(player: Player, color: Color, position: Position) : Piece("Rook", player, color, position) {

    override fun allowedNextPositions(board: Board): List<Position> {
        val current = board.findPositionOfPiece(this)
                ?: throw RuntimeException("Position of Figure $this not found")
        val allowedOnEmptyBoard = generallyAllowedForPiece(current)
        val result = mutableListOf(*allowedOnEmptyBoard.toTypedArray())

        // same rank
        removeProhibitedPositions(result, current, board, (current.file + 1)..7, true) // files to the right
        removeProhibitedPositions(result, current, board, (current.file - 1 downTo 0), true) // files to the left

        // same file
        removeProhibitedPositions(result, current, board, (current.rank + 1)..7, false) // ranks upward
        removeProhibitedPositions(result, current, board, (current.rank - 1 downTo 0), false) // ranks downward

        return result
    }

    private fun removeProhibitedPositions(
            result: MutableList<Position>,
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
                if (pieceAtPos.color == color) {
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

    private fun generallyAllowedForPiece(current: Position): List<Position> {
        val allowedOnSameRank = mutableListOf<Position>()
        for (num in 0 until current.file) allowedOnSameRank.add(Position(num, current.rank))
        for (num in current.file..7) allowedOnSameRank.add(Position(num, current.rank))
        val allowedOnSameFile = mutableListOf<Position>()
        for (num in 0 until current.rank) allowedOnSameFile.add(Position(current.file, num))
        for (num in current.rank..7) allowedOnSameFile.add(Position(current.file, num))
        return listOf(*allowedOnSameRank.toTypedArray(), *allowedOnSameFile.toTypedArray())
                .filter { it != current }
    }
}
