package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RookTest {

    @Test
    fun `allowed next positions when in the center on an empty board`() {
        val rook = Rook(Color.BLACK, Position(3, 3))
        val board = Board(false)
        board.putPiece(rook)
        val actual = rook.allowedNextPositions(board);
        assertThat(actual).containsExactlyInAnyOrder(
                Position(0, 3), Position(1, 3), Position(2, 3),
                Position(4, 3), Position(5, 3), Position(6, 3), Position(7, 3),
                Position(3, 0), Position(3, 1), Position(3, 2),
                Position(3, 4), Position(3, 5), Position(3, 6), Position(3, 7))
    }

    @Test
    fun `allowed next positions when with opponent to the right`() {
        val rook = Rook(Color.BLACK, Position(3, 3))
        val otherRook = Rook(Color.WHITE, Position(5, 3))
        val board = Board(false)
        board.putPiece(rook)
        board.putPiece(otherRook)
        println(board)
        val actual = rook.allowedNextPositions(board);
        assertThat(actual).containsExactlyInAnyOrder(
                Position(0, 3), Position(1, 3), Position(2, 3),
                Position(4, 3), Position(5, 3), /*Position(6, 3), Position(7, 3),*/
                Position(3, 0), Position(3, 1), Position(3, 2),
                Position(3, 4), Position(3, 5), Position(3, 6), Position(3, 7))
    }

    @Test
    fun `allowed next positions when with opponent to the left`() {
        val rook = Rook(Color.BLACK, Position(3, 3))
        val otherRook = Rook(Color.WHITE, Position(1, 3))
        val board = Board(false)
        board.putPiece(rook)
        board.putPiece(otherRook)
        println(board)
        val actual = rook.allowedNextPositions(board);
        assertThat(actual).containsExactlyInAnyOrder(
                /*Position(0, 3), */Position(1, 3), Position(2, 3),
                Position(4, 3), Position(5, 3), Position(6, 3), Position(7, 3),
                Position(3, 0), Position(3, 1), Position(3, 2),
                Position(3, 4), Position(3, 5), Position(3, 6), Position(3, 7))
    }

    @Test
    fun `allowed next positions when with opponent to the top`() {
        val rook = Rook(Color.BLACK, Position(3, 3))
        val otherRook = Rook(Color.WHITE, Position(3, 1))
        val board = Board(false)
        board.putPiece(rook)
        board.putPiece(otherRook)
        println(board)
        val actual = rook.allowedNextPositions(board);
        assertThat(actual).containsExactlyInAnyOrder(
                Position(0, 3), Position(1, 3), Position(2, 3),
                Position(4, 3), Position(5, 3), Position(6, 3), Position(7, 3),
                /*Position(3, 0), */Position(3, 1), Position(3, 2),
                Position(3, 4), Position(3, 5), Position(3, 6), Position(3, 7))
    }

    @Test
    fun `allowed next positions when with opponent to the bottom`() {
        val rook = Rook(Color.BLACK, Position(3, 3))
        val otherRook = Rook(Color.WHITE, Position(3, 5))
        val board = Board(false)
        board.putPiece(rook)
        board.putPiece(otherRook)
        println(board)
        val actual = rook.allowedNextPositions(board);
        assertThat(actual).containsExactlyInAnyOrder(
                Position(0, 3), Position(1, 3), Position(2, 3),
                Position(4, 3), Position(5, 3), Position(6, 3), Position(7, 3),
                Position(3, 0), Position(3, 1), Position(3, 2),
                Position(3, 4), Position(3, 5)/*, Position(3, 6), Position(3, 7)*/)
    }

    @Test
    fun `allowed next positions in a complex situation`() {
        val board = Board(false)
        val rook = Rook(Color.BLACK, Position(3, 3))
        board.putPiece(rook)
        arrayOf(
                Rook(Color.WHITE, Position(0, 1)),
                Rook(Color.BLACK, Position(4, 5)),
                Rook(Color.WHITE, Position(4, 6)),
                Rook(Color.BLACK, Position(2, 4)),
                Rook(Color.WHITE, Position(2, 3)),
                Rook(Color.BLACK, Position(1, 1)),
                Rook(Color.WHITE, Position(3, 7)),
                Rook(Color.BLACK, Position(2, 0)),
        ).forEach { board.putPiece(it) }
        println(board)
        assertThat(rook.allowedNextPositions(board)).containsExactlyInAnyOrder(
                Position(3, 2), Position(3, 1), Position(3, 0), Position(4, 3),
                Position(5, 3), Position(6, 3), Position(7, 3), Position(3, 4),
                Position(3, 5), Position(3, 6), Position(3, 7), Position(2, 3))
    }
}
