package io.zemke.javalinchess.chess

import io.zemke.javalinchess.chess.piece.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BoardTest {

    @Test
    fun `rook move right`() {
        val board = Board(false)
        val source = Position(3, 3)
        board.putPiece(Rook(Color.BLACK, source))
        val piece = board.findPiece(source)
        assertThat(piece).isNotNull
        assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(3, 5)
        val actual = board.move(piece!!, target)
        assertThat(actual.findPiece(target)).isEqualTo(piece)
        assertThat(actual.findPiece(source)).isNull()
    }

    @Test
    fun `rook move left`() {
        val board = Board(false)
        val source = Position(3, 3)
        board.putPiece(Rook(Color.BLACK, source))
        val piece = board.findPiece(source)
        assertThat(piece).isNotNull
        assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(3, 1)
        val actual = board.move(piece!!, target)
        assertThat(actual.findPiece(target)).isEqualTo(piece)
        assertThat(actual.findPiece(source)).isNull()
    }

    @Test
    fun `rook move up`() {
        val board = Board(false)
        val source = Position(3, 3)
        board.putPiece(Rook(Color.BLACK, source))
        val piece = board.findPiece(source)
        assertThat(piece).isNotNull
        assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(5, 3)
        val actual = board.move(piece!!, target)
        assertThat(actual.findPiece(target)).isEqualTo(piece)
        assertThat(actual.findPiece(source)).isNull()
    }

    @Test
    fun `rook move down`() {
        val source = Position(3, 3)
        val board = Board(false)
        board.putPiece(Rook(Color.BLACK, source))
        val piece = board.findPiece(source)
        assertThat(piece).isNotNull
        assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(1, 3)
        val actual = board.move(piece!!, target)
        assertThat(actual.findPiece(target)).isEqualTo(piece)
        assertThat(actual.findPiece(source)).isNull()
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
        assertThat(board.findPiece(Position(0, 3))).isNull()
        assertThat(board.findPieceById(passible.id)).isNull()
        assertThat(board.findPiece(Position(0, 2))).isEqualTo(passer)
        assertThat(board.captured).containsExactly(passible)
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
        assertThat(king.castlingAllowed(board, rookKingside)).isTrue
        assertThat(board.castlingAllowed)
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
        assertThat(king.castlingAllowed(board, rookKingside)).isTrue
        assertThat(board.castlingAllowed)
                .containsExactly(Rook.Side.KINGSIDE, Rook.Side.QUEENSIDE)
    }

    @Test
    fun `is in check`() {
        val board = Board(false)
        val kingInCheck = King(Color.BLACK, Position(3, 3))
        board.putPiece(kingInCheck)
        board.putPiece(Rook(Color.WHITE, Position(3, 0)))
        assertThat(kingInCheck.inCheck(board)).isTrue()
        assertThat(board.checkmated).isFalse()
    }

    @Test
    fun `is not in check because own Piece`() {
        val board = Board(false)
        val kingInCheck = King(Color.BLACK, Position(3, 3))
        board.putPiece(kingInCheck)
        board.putPiece(Rook(Color.BLACK, Position(3, 0)))
        assertThat(kingInCheck.inCheck(board)).isFalse()
        assertThat(board.checkmated).isFalse()
    }

    @Test
    fun `is not checkmated`() {
        val board = Board(false)
        val kingInCheck = King(Color.BLACK, Position(3, 3))
        board.putPiece(kingInCheck)
        board.putPiece(Rook(Color.WHITE, Position(3, 0)))
        board.putPiece(Rook(Color.WHITE, Position(0, 3)))
        board.putPiece(Queen(Color.WHITE, Position(1, 1)))
        board.putPiece(Bishop(Color.WHITE, Position(5, 1)))
        println(board)
        assertThat(kingInCheck.inCheck(board)).isTrue()
        assertThat(board.checkmated).isFalse()
    }
}
