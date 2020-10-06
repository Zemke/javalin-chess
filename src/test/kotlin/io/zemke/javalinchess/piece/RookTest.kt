package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RookTest {

    @Test
    fun `allowed next positions when in the center on an empty board`() {
        val rook = Rook(Player("p1"), Color.BLACK, Position(3, 3))
        val board = Board()
        board.putPiece(rook)
        val actual = rook.allowedNextPositions(board);
        assertThat(actual).containsExactlyInAnyOrder(
                Position(0, 3), Position(1, 3), Position(2, 3),
                Position(4, 3), Position(5, 3), Position(6, 3), Position(7, 3),
                Position(3, 0), Position(3, 1), Position(3, 2),
                Position(3, 4), Position(3, 5), Position(3, 6), Position(3, 7))
    }
}

