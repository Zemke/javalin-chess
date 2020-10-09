package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Entity
import io.zemke.javalinchess.controller.Player

abstract class Piece(val name: String, val player: Player, val color: Color, val position: Position) : Entity() {

    abstract fun allowedNextPositions(board: Board): Set<Position>

    override fun toString(): String {
        return "Piece(id='$id', name='$name', player=$player, color=$color, position=$position)"
    }

    // todo can be used in some places
    fun isOwn(color: Color): Boolean = color == this.color
}
