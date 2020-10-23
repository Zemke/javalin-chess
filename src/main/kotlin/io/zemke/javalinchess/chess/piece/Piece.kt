package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.Color.BLACK
import io.zemke.javalinchess.chess.piece.Color.WHITE
import io.zemke.javalinchess.complex.Entity
import kotlin.contracts.contract

abstract class Piece(val name: String, val color: Color, val position: Position) : Entity() {

    abstract fun allowedNextPositions(board: Board): Set<Position>

    override fun toString(): String {
        return "Piece(id='$id', name='$name', color=$color, position=$position)"
    }

    fun isOwn(color: Color): Boolean = color == this.color

    fun promotable(board: Board): Boolean {
        contract { returns() implies (this@Piece is Pawn) }
        if (this !is Pawn) return false
        val (_, rank) = board.findPosition(this)
        return ((color == BLACK && rank == 7)
                || (color == WHITE && rank == 0))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Piece

        if (name != other.name) return false
        if (color != other.color) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + position.hashCode()
        return result
    }
}
