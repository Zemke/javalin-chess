package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KnightTest {

    @Test
    fun `next allowed positions on an empty board`() {
        val board = Board()
        val knight = Knight(Player("p1"), Color.BLACK, Position(3, 3))
        board.putPiece(knight)
        val actual = knight.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(
                Position(3, 4), Position(3, 2), Position(2, 3), Position(4, 3),
                Position(2, 2), Position(4, 4), Position(4, 2), Position(2, 4))
    }

    @Test
    fun `next allowed positions in more complex situation`() {
        val board = Board()
        val p1 = Player("p1")
        val p2 = Player("p2")
        val knight = Knight(p1, Color.BLACK, Position(3, 3))
        setOf(
                Rook(p2, Color.WHITE, Position(2, 1)),
                Rook(p1, Color.BLACK, Position(2, 3)),
                Rook(p2, Color.WHITE, Position(4, 4)))
                .forEach { board.putPiece(it) }
        board.putPiece(knight)
        val actual = knight.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(
                Position(3, 4), Position(3, 2), /*Position(2, 3), */Position(4, 3),
                Position(2, 2), Position(4, 4), Position(4, 2), Position(2, 4))
    }
}
