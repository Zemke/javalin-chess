package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KnightTest {

    @Test
    fun `allowed positions on an empty board`() {
        val knight = Knight(Color.BLACK, Position(3, 3))
        val board = Board(false)
        board.putPiece(knight)
        val actual = knight.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(
                Position(5, 2), Position(5, 4), Position(1, 2), Position(1, 4),
                Position(2, 5), Position(4, 5), Position(2, 1), Position(4, 1))
    }

    @Test
    fun `allowed positions on a complex board`() {
        val knight = Knight(Color.BLACK, Position(3, 3))
        val board = Board(false)
        board.putPiece(knight)
        setOf(
                Knight(Color.BLACK, Position(5, 4)),
                Knight(Color.WHITE, Position(2, 5)),
                Knight(Color.BLACK, Position(4, 4)),
                Knight(Color.WHITE, Position(2, 2)),
        )
                .forEach { board.putPiece(it) }
        val actual = knight.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(
                Position(5, 2), /*Position(5, 4), */Position(1, 2), Position(1, 4),
                Position(2, 5), Position(4, 5), Position(2, 1), Position(4, 1))
    }
}
