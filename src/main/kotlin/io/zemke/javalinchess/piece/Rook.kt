package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class Rook(player: Player, color: Color, position: Position) : Piece("Rook", player, color, position) {

    override fun allowedNextPositions(board: Board): List<Position> {
        var allowedOnSameRank = mutableListOf<Position>()
        val current = board.findPositionOfPiece(this)
                ?: throw RuntimeException("Position of Figure $this not found")
        for (num in 0 until current.file) allowedOnSameRank.add(Position(num, current.rank))
        for (num in current.file..7) allowedOnSameRank.add(Position(num, current.rank))
        allowedOnSameRank = allowedOnSameRank.filter { it != current }.toMutableList()
        var rankBroken = false
        allowedOnSameRank = allowedOnSameRank.filter {
            if (rankBroken) false
            val res = board.getPieceAt(it) == null
            rankBroken = res;
            res
        }.toMutableList()
        var allowedOnSameFile = mutableListOf<Position>()
        for (num in 0 until current.rank) allowedOnSameFile.add(Position(current.file, num))
        for (num in current.rank..7) allowedOnSameFile.add(Position(current.file, num))
        var fileBroken = false
        allowedOnSameFile = allowedOnSameFile.filter {
            if (fileBroken) false
            val res = board.getPieceAt(it) == null
            fileBroken = res;
            res
        }.toMutableList()
        return listOf(*allowedOnSameRank.toTypedArray(), *allowedOnSameFile.toTypedArray())
    }
}
