package io.zemke.javalinchess.figure

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Entity
import io.zemke.javalinchess.controller.Player

// todo rename to piece
abstract class Figure(val name: String, val player: Player, val color: Color, val position: Position) : Entity() {

    abstract fun move(board: Board, target: Position): Board

    override fun toString(): String {
        return "Figure(id='$id', name='$name', player=$player, color=$color, position=$position)"
    }

    class InvalidMoveException(figure: Figure, current: Position, target: Position)
        : RuntimeException("Figure $figure cannot move from $current to $target")
}
