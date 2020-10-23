package io.zemke.javalinchess.chess

import io.zemke.javalinchess.chess.piece.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BoardTest {

    @Test
    fun `rook move right`() {
        val board = Board(false)
        val source = Position(3, 3)
        board.putPiece(Rook(Color.BLACK, source))
        val piece = board.findPiece(source)
        Assertions.assertThat(piece).isNotNull
        Assertions.assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(3, 5)
        val actual = board.move(piece!!, target)
        Assertions.assertThat(actual.findPiece(target)).isEqualTo(piece)
        Assertions.assertThat(actual.findPiece(source)).isNull()
    }

    @Test
    fun `rook move left`() {
        val board = Board(false)
        val source = Position(3, 3)
        board.putPiece(Rook(Color.BLACK, source))
        val piece = board.findPiece(source)
        Assertions.assertThat(piece).isNotNull
        Assertions.assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(3, 1)
        val actual = board.move(piece!!, target)
        Assertions.assertThat(actual.findPiece(target)).isEqualTo(piece)
        Assertions.assertThat(actual.findPiece(source)).isNull()
    }

    @Test
    fun `rook move up`() {
        val board = Board(false)
        val source = Position(3, 3)
        board.putPiece(Rook(Color.BLACK, source))
        val piece = board.findPiece(source)
        Assertions.assertThat(piece).isNotNull
        Assertions.assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(5, 3)
        val actual = board.move(piece!!, target)
        Assertions.assertThat(actual.findPiece(target)).isEqualTo(piece)
        Assertions.assertThat(actual.findPiece(source)).isNull()
    }

    @Test
    fun `rook move down`() {
        val source = Position(3, 3)
        val board = Board(false)
        board.putPiece(Rook(Color.BLACK, source))
        val piece = board.findPiece(source)
        Assertions.assertThat(piece).isNotNull
        Assertions.assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(1, 3)
        val actual = board.move(piece!!, target)
        Assertions.assertThat(actual.findPiece(target)).isEqualTo(piece)
        Assertions.assertThat(actual.findPiece(source)).isNull()
    }

    @Test
    fun `en passant`() {
        val passer = Pawn(Color.WHITE, Position(1, 3))
        val board = Board(false)
        val passible = Pawn(Color.BLACK, Position(0, 1))
        board.putPieces(passer, passible)
        board.move(passible, Position(0, 3))
        println(board)
        board.move(passer, Position(0, 2))
        println(board)
        Assertions.assertThat(board.findPiece(Position(0, 3))).isNull()
        Assertions.assertThat(board.findPieceById(passible.id)).isNull()
        Assertions.assertThat(board.findPiece(Position(0, 2))).isEqualTo(passer)
    }

    @Test
    fun `nextTurn white`() {
        val board = Board(false)
        val king = King(Color.WHITE, Position(4, 7))
        val rookQueenside = Rook(Color.WHITE, Position(0, 7))
        val rookKingside = Rook(Color.WHITE, Position(7, 7))
        board.putPiece(king)
        board.putPiece(rookQueenside)
        board.putPiece(rookKingside)
        board.nextTurn()
        board.nextTurn()
        println("$board")
        Assertions.assertThat(king.castlingAllowed(board, rookKingside)).isTrue
        Assertions.assertThat(board.castlingAllowed)
                .containsExactly(Rook.Side.KINGSIDE, Rook.Side.QUEENSIDE)
    }

    @Test
    fun `nextTurn black`() {
        val board = Board(false)
        val king = King(Color.BLACK, Position(4, 0))
        val rookQueenside = Rook(Color.BLACK, Position(0, 0))
        val rookKingside = Rook(Color.BLACK, Position(7, 0))
        board.putPiece(king)
        board.putPiece(rookQueenside)
        board.putPiece(rookKingside)
        board.nextTurn()
        println("$board")
        Assertions.assertThat(king.castlingAllowed(board, rookKingside)).isTrue
        Assertions.assertThat(board.castlingAllowed)
                .containsExactly(Rook.Side.KINGSIDE, Rook.Side.QUEENSIDE)
    }
}
