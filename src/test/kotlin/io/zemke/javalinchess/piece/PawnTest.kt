package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PawnTest {

    @Test
    fun `allowed next positions on an empty board`() {
        val pawn = Pawn(Color.BLACK, Position(3, 3))
        val board = Board(false)
        board.putPiece(pawn)
        println(board)
        val actual = pawn.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(Position(3, 4))
    }

    @Test
    fun `allowed next positions on an empty board from a starting position`() {
        val pawn = Pawn(Color.BLACK, Position(3, 1))
        val board = Board(false)
        board.putPiece(pawn)
        val actual = pawn.allowedNextPositions(board)
        println(board)
        assertThat(actual).containsExactlyInAnyOrder(Position(3, 2), Position(3, 3))
    }

    @Test
    fun `allowed next positions with capturing possibilities`() {
        val pawn = Pawn(Color.WHITE, Position(3, 6))
        val board = Board(false)
        board.putPiece(pawn)
        board.putPiece(Pawn(Color.WHITE, Position(2, 5)))
        board.putPiece(Pawn(Color.BLACK, Position(4, 5)))
        println(board)
        val actual = pawn.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(
                Position(3, 5), Position(3, 4), Position(4, 5))
    }

    @Test
    fun `allowed next positions with en passant`() {
        val pawn = Pawn(Color.WHITE, Position(3, 3))
        val board = Board(false)
        board.putPiece(pawn)
        val passible = Pawn(Color.BLACK, Position(4, 1))
        board.putPiece(passible)
        assertThat(passible.allowedNextPositions(board)).contains(Position(4, 3))
        board.move(passible, Position(4, 3))
        println(board)
        val actual = pawn.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(Position(4, 2), Position(3, 2))
    }

    @Test
    fun `promote to queen`() {
        val board = Board(false)
        val position = Position(4, 7)
        val pawn = Pawn(Color.BLACK, position)
        board.putPiece(pawn)
        assertThat(pawn.promotable(board)).isTrue()
        var queen: Queen? = null
        pawn.promote(board) { c, p ->
            queen = Queen(c, p)
            queen!!
        }
        assertThat(board.getPieceAt(position)).isEqualTo(queen)
    }

    @Test
    fun `is promotable`() {
        val board = Board(false)
        val position = Position(4, 5)
        val pawn = Pawn(Color.BLACK, position)
        board.putPiece(pawn)
        assertThat(pawn.promotable(board)).isFalse()
    }
}
