package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class KingTest {

    @Test
    fun `next allowed positions on an empty board`() {
        val board = Board(false)
        val king = King(Color.BLACK, Position(3, 3))
        board.putPiece(king)
        val actual = king.allowedNextPositions(board)
        Assertions.assertThat(actual).containsExactlyInAnyOrder(
                Position(3, 4), Position(3, 2), Position(2, 3), Position(4, 3),
                Position(2, 2), Position(4, 4), Position(4, 2), Position(2, 4))
    }

    @Test
    fun `next allowed positions in more complex situation`() {
        val board = Board(false)
        val king = King(Color.BLACK, Position(3, 3))
        setOf(
                Rook(Color.WHITE, Position(2, 1)),
                Rook(Color.BLACK, Position(2, 3)),
                Rook(Color.WHITE, Position(4, 4)))
                .forEach { board.putPiece(it) }
        board.putPiece(king)
        val actual = king.allowedNextPositions(board)
        Assertions.assertThat(actual).containsExactlyInAnyOrder(
                Position(3, 4), Position(3, 2), /*Position(2, 3), */Position(4, 3),
                Position(2, 2), Position(4, 4), Position(4, 2), Position(2, 4))
    }
}
