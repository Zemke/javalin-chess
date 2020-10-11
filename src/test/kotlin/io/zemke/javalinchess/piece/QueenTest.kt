package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class QueenTest {

    @Test
    fun `allowed positions on an empty board`() {
        val queen = Queen(Color.BLACK, Position(3, 3))
        val board = Board(false)
        board.putPiece(queen)
        val actual = queen.allowedNextPositions(board)
        println(board)
        assertThat(actual).containsExactlyInAnyOrder(
                Position(0, 3), Position(1, 3), Position(2, 3),
                Position(4, 3), Position(5, 3), Position(6, 3),
                Position(7, 3), Position(3, 0), Position(3, 1),
                Position(3, 2), Position(3, 4), Position(3, 5),
                Position(3, 6), Position(3, 7), Position(4, 4),
                Position(5, 5), Position(6, 6), Position(7, 7),
                Position(2, 2), Position(1, 1), Position(0, 0),
                Position(4, 2), Position(5, 1), Position(6, 0),
                Position(2, 4), Position(1, 5), Position(0, 6))
    }

    @Test
    fun `allowed positions on a complex board`() {
        val queen = Queen(Color.BLACK, Position(3, 3))
        val board = Board(false)
        board.putPiece(queen)
        setOf(
                Queen(Color.BLACK, Position(3, 4)),
                Queen(Color.BLACK, Position(3, 2)),
                Queen(Color.BLACK, Position(4, 1)),
                Queen(Color.BLACK, Position(0, 4)),
                Queen(Color.BLACK, Position(2, 6)),
                Queen(Color.WHITE, Position(1, 3)),
                Queen(Color.BLACK, Position(6, 0)))
                .forEach { board.putPiece(it) }
        println(board)
        val actual = queen.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(
                /*Position(0, 3), */Position(1, 3), Position(2, 3),
                Position(4, 3), Position(5, 3), Position(6, 3),
                Position(7, 3), /*Position(3, 0), Position(3, 1),
                Position(3, 2), Position(3, 4), Position(3, 5),
                Position(3, 6), Position(3, 7), */Position(4, 4),
                Position(5, 5), Position(6, 6), Position(7, 7),
                Position(2, 2), Position(1, 1), Position(0, 0),
                Position(4, 2), Position(5, 1), /*Position(6, 0),*/
                Position(2, 4), Position(1, 5), Position(0, 6))
    }
}




























