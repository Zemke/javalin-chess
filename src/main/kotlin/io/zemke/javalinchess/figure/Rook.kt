package io.zemke.javalinchess.figure

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class Rook(player: Player, color: Color, position: Position) : Figure("Rook", player, color, position) {

    override fun move(board: Board, target: Position): Board {
        // todo jump over others
        // todo check if it captures an opponent's piece
        // todo cannot capture an own piece
        // todo should be split into two function; getting the allowed moves and performing the move
        //  for better testability
        var allowed = mutableListOf<Position>()
        val current = board.findPositionOfFigure(this)
                ?: throw RuntimeException("Position of Figure $this not found")
        for (num in 0 until current.file) allowed.add(Position(num, current.rank))
        for (num in current.file..7) allowed.add(Position(num, current.rank))
        for (num in 0 until current.rank) allowed.add(Position(current.file, num))
        for (num in current.rank..7) allowed.add(Position(current.file, num))
        allowed = allowed.filter { it != current }.toMutableList()
        if (!allowed.contains(target)) {
            throw InvalidMoveException(this, current, target)
        }
        return board.move(this, target)
    }
}
