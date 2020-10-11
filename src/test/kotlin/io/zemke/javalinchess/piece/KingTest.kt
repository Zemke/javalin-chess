package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KingTest {

    @Test
    fun `next allowed positions on an empty board`() {
        val board = Board(false)
        val king = King(Color.BLACK, Position(3, 3))
        board.putPiece(king)
        val actual = king.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(
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
        assertThat(actual).containsExactlyInAnyOrder(
                Position(3, 4), Position(3, 2), /*Position(2, 3), */Position(4, 3),
                Position(2, 2), Position(4, 4), Position(4, 2), Position(2, 4))
    }

    @Test
    fun `castle kingside`() {
        val board = Board(false)
        val king = King(Color.BLACK, Position(4, 0))
        val rookQueenside = Rook(Color.BLACK, Position(0, 0))
        val rookKingside = Rook(Color.BLACK, Position(7, 0))
        board.putPiece(king)
        board.putPiece(rookQueenside)
        board.putPiece(rookKingside)
        println("before castling\n$board")
        assertThat(king.castlingAllowed(board, rookKingside)).isTrue()
        king.castle(board, rookKingside)
        println("after castling\n$board")
        assertThat(board.getPieceAt(Position(6, 0))).isEqualTo(king)
        assertThat(board.getPieceAt(Position(5, 0))).isEqualTo(rookKingside)
        assertThat(board.getPieceAt(Position(0, 0))).isEqualTo(rookQueenside)
    }

    @Test
    fun `castle queenside`() {
        val board = Board(false)
        val king = King(Color.BLACK, Position(4, 0))
        val rookQueenside = Rook(Color.BLACK, Position(0, 0))
        val rookKingside = Rook(Color.BLACK, Position(7, 0))
        board.putPiece(king)
        board.putPiece(rookQueenside)
        board.putPiece(rookKingside)
        println("before castling\n$board")
        assertThat(king.castlingAllowed(board, rookQueenside)).isTrue()
        king.castle(board, rookQueenside)
        println("after castling\n$board")
        assertThat(board.getPieceAt(Position(2, 0))).isEqualTo(king)
        assertThat(board.getPieceAt(Position(7, 0))).isEqualTo(rookKingside)
        assertThat(board.getPieceAt(Position(3, 0))).isEqualTo(rookQueenside)
    }
}
