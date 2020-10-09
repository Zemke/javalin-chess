package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BishopTest {

    @Test
    fun `next allowed positions on an empty board`() {
        val bishop = Bishop(Player("p1"), Color.BLACK, Position(3, 3))
        val board = Board()
        board.putPiece(bishop)
        val result = bishop.allowedNextPositions(board)
        assertThat(result).containsExactlyInAnyOrder(
                Position(4, 4), Position(5, 5), Position(6, 6),
                Position(7, 7), Position(2, 2), Position(1, 1),
                Position(0, 0), Position(4, 2), Position(5, 1),
                Position(6, 0), Position(2, 4), Position(1, 5), Position(0, 6))
    }

    @Test
    fun `next allowed positions on a complex board`() {
        val player1 = Player("p1")
        val player2 = Player("p2")
        val bishop = Bishop(player1, Color.BLACK, Position(3, 3))
        val board = Board()
        board.putPiece(bishop)
        setOf(
                Bishop(player1, Color.BLACK, Position(3, 5)),
                Bishop(player2, Color.WHITE, Position(1, 5)),
                Bishop(player1, Color.BLACK, Position(6, 5)),
                Bishop(player2, Color.WHITE, Position(6, 2)),
                Bishop(player1, Color.BLACK, Position(0, 1)),
                Bishop(player2, Color.WHITE, Position(3, 7)),
                Bishop(player1, Color.BLACK, Position(5, 1)))
                .forEach { board.putPiece(it) }
        println(board)
        val result = bishop.allowedNextPositions(board)
        assertThat(result).containsExactlyInAnyOrder(
                Position(4, 4), Position(5, 5), Position(6, 6),
                Position(7, 7), Position(2, 2), Position(1, 1),
                Position(0, 0), Position(4, 2), /*Position(5, 1),
                Position(6, 0), */Position(2, 4), Position(1, 5)/*, Position(0, 6)*/)
    }
}
