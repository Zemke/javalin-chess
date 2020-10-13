package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.complex.Entity

abstract class Piece(val name: String, val color: Color, val position: Position) : Entity() {

    abstract fun allowedNextPositions(board: Board): Set<Position>

    override fun toString(): String {
        return "Piece(id='$id', name='$name', color=$color, position=$position)"
    }

    fun isOwn(color: Color): Boolean = color == this.color
}
