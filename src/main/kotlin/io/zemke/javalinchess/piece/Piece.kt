package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Entity
import io.zemke.javalinchess.controller.Player

abstract class Piece(val name: String, val player: Player, val color: Color, val position: Position) : Entity() {

    abstract fun move(board: Board, target: Position): Board

    override fun toString(): String {
        return "Piece(id='$id', name='$name', player=$player, color=$color, position=$position)"
    }

    class InvalidMoveException(piece: Piece, current: Position, target: Position)
        : RuntimeException("Piece $piece cannot move from $current to $target")
}
