package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board
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
        println(board)
        val actual = king.allowedNextPositions(board)
        assertThat(actual).containsExactlyInAnyOrder(
                /*Position(3, 4), */Position(3, 2), /*Position(2, 3), Position(4, 3),
                Position(2, 2), */Position(4, 4)/*, Position(4, 2), Position(2, 4)*/)
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
        assertThat(board.findPiece(Position(6, 0))).isEqualTo(king)
        assertThat(board.findPiece(Position(5, 0))).isEqualTo(rookKingside)
        assertThat(board.findPiece(Position(0, 0))).isEqualTo(rookQueenside)
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
        assertThat(board.findPiece(Position(2, 0))).isEqualTo(king)
        assertThat(board.findPiece(Position(7, 0))).isEqualTo(rookKingside)
        assertThat(board.findPiece(Position(3, 0))).isEqualTo(rookQueenside)
    }

    @Test
    fun `King mustn't move through check when castling queenside`() {
        val board = Board(false)
        val king = King(Color.BLACK, Position(4, 0))
        val rookQueenside = Rook(Color.BLACK, Position(0, 0))
        val rookKingside = Rook(Color.BLACK, Position(7, 0))
        val opponentRook = Rook(Color.WHITE, Position(3, 4))
        board.putPieces(king, rookQueenside, rookKingside, opponentRook)
        println(board)
        assertThat(king.castlingAllowed(board, rookQueenside)).isFalse()
    }

    @Test
    fun `King mustn't move through check when castling kingside`() {
        val board = Board(false)
        val king = King(Color.BLACK, Position(4, 0))
        val rookQueenside = Rook(Color.BLACK, Position(0, 0))
        val rookKingside = Rook(Color.BLACK, Position(7, 0))
        val opponentRook = Rook(Color.WHITE, Position(5, 0))
        board.putPieces(king, rookQueenside, rookKingside, opponentRook)
        println(board)
        assertThat(king.castlingAllowed(board, rookKingside)).isFalse()
    }

    @Test
    fun `King mustn't castle when the involved rook or King itself have already moved`() {
        val board = Board(false)
        val rookQueenside = Rook(Color.BLACK, Position(0, 0))
        val rookKingside = Rook(Color.BLACK, Position(7, 0))
        val king = King(Color.BLACK, Position(4, 0))
        board.putPieces(rookQueenside, rookKingside, king)
        board.move(rookQueenside, Position(0, 3)).move(rookQueenside, Position(0, 0))
        assertThat(king.castlingAllowed(board, rookQueenside)).isFalse()
        assertThat(king.castlingAllowed(board, rookKingside)).isTrue()
        board.move(king, Position(4, 1)).move(king, Position(4, 0))
        assertThat(king.castlingAllowed(board, rookQueenside)).isFalse()
        assertThat(king.castlingAllowed(board, rookKingside)).isFalse()
    }
}
