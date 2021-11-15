package io.zemke.javalinchess.chess

import io.zemke.javalinchess.chess.piece.*
import io.zemke.javalinchess.chess.piece.Color.BLACK
import io.zemke.javalinchess.chess.piece.Color.WHITE
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BoardTest {

    @Test
    fun `rook move right`() {
        val board = Board(false)
        val source = Position(3, 3)
        board.putPiece(Rook(BLACK, source))
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
        board.putPiece(Rook(BLACK, source))
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
        board.putPiece(Rook(BLACK, source))
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
        board.putPiece(Rook(BLACK, source))
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
        val passer = Pawn(WHITE, Position(1, 3))
        val board = Board(false)
        val passible = Pawn(BLACK, Position(0, 1))
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

    private class Castle(val rookQ: Rook, val rookK: Rook, val K: King)

    @Test
    fun `castling nextTurn white`() {
        val board = Board(false)
        val castle = castlingForWhite(board)
        println("$board")
        assertThat(castle.K.castlingAllowed(board, castle.rookK)).isTrue
        assertThat(board.castlingAllowed[WHITE])
                .containsExactlyInAnyOrder(Rook.Side.KINGSIDE, Rook.Side.QUEENSIDE)
    }

    @Test
    fun `castling nextTurn black`() {
        val board = Board(false)
        val castle = castlingForBlack(board)
        println("$board")
        assertThat(castle.K.castlingAllowed(board, castle.rookK)).isTrue
        assertThat(board.castlingAllowed[BLACK])
                .containsExactlyInAnyOrder(Rook.Side.KINGSIDE, Rook.Side.QUEENSIDE)
    }

    @Test
    fun `castling independent of turn`() {
        val board = Board(false)
        val castleWhite = castlingForWhite(board)
        val castleBlack = castlingForBlack(board)
        println("$board")
        assertThat(castleWhite.K.castlingAllowed(board, castleWhite.rookK)).isTrue
        assertThat(board.castlingAllowed[WHITE])
                .containsExactlyInAnyOrder(Rook.Side.KINGSIDE, Rook.Side.QUEENSIDE)
        assertThat(castleBlack.K.castlingAllowed(board, castleBlack.rookK)).isTrue
        assertThat(board.castlingAllowed[BLACK])
                .containsExactlyInAnyOrder(Rook.Side.KINGSIDE, Rook.Side.QUEENSIDE)
    }

    private fun castlingForWhite(board: Board): Castle {
        val king = King(WHITE, Position(4, 7))
        val rookQueenside = Rook(WHITE, Position(0, 7))
        val rookKingside = Rook(WHITE, Position(7, 7))
        board.putPiece(king)
        board.putPiece(rookQueenside)
        board.putPiece(rookKingside)
        board.nextTurn()
        board.nextTurn()
        return Castle(K=king, rookQ=rookQueenside, rookK=rookKingside)
    }

    private fun castlingForBlack(board: Board): Castle {
        val king = King(BLACK, Position(4, 0))
        val rookQueenside = Rook(BLACK, Position(0, 0))
        val rookKingside = Rook(BLACK, Position(7, 0))
        board.putPiece(king)
        board.putPiece(rookQueenside)
        board.putPiece(rookKingside)
        board.nextTurn()
        return Castle(K=king, rookQ=rookQueenside, rookK=rookKingside)
    }

    @Test
    fun `is in check`() {
        val board = Board(false)
        val kingInCheck = King(BLACK, Position(3, 3))
        board.putPiece(kingInCheck)
        board.putPiece(Rook(WHITE, Position(3, 0)))
        board.nextTurn()
        assertThat(board.nextTurn).isEqualTo(BLACK)
        assertThat(kingInCheck.inCheck(board)).isTrue()
        assertThat(board.checkmated).isFalse()
    }

    @Test
    fun `is not in check because own Piece`() {
        val board = Board(false)
        val kingInCheck = King(BLACK, Position(3, 3))
        board.putPiece(kingInCheck)
        board.putPiece(Rook(BLACK, Position(3, 0)))
        assertThat(board.nextTurn).isEqualTo(WHITE)
        assertThat(kingInCheck.inCheck(board)).isFalse()
        assertThat(board.checkmated).isFalse()
        board.nextTurn()
        assertThat(board.nextTurn).isEqualTo(BLACK)
        assertThat(kingInCheck.inCheck(board)).isFalse()
        assertThat(board.checkmated).isFalse()
    }

    @Test
    fun `is not checkmated`() {
        val board = Board(false)
        val kingInCheck = King(BLACK, Position(3, 3))
        board.putPiece(kingInCheck)
        board.putPiece(Rook(BLACK, Position(3, 0)))
        board.putPiece(Rook(WHITE, Position(0, 3)))
        board.putPiece(Queen(WHITE, Position(1, 2)))
        board.nextTurn()
        println(board)
        assertThat(board.nextTurn).isEqualTo(BLACK)
        assertThat(kingInCheck.inCheck(board)).isTrue()
        assertThat(board.checkmated).isFalse()
    }

    @Test
    fun `is not checkmated 2`() {
        val board = Board(false)
        val kingInCheck = King(BLACK, Position(3, 3))
        board.putPiece(kingInCheck)
        board.putPiece(Rook(BLACK, Position(3, 0)))
        board.putPiece(Rook(WHITE, Position(0, 3)))
        board.putPiece(Pawn(WHITE, Position(1, 2)))
        board.nextTurn()
        println(board)
        assertThat(board.nextTurn).isEqualTo(BLACK)
        assertThat(kingInCheck.inCheck(board)).isTrue()
        assertThat(board.checkmated).isFalse()
    }

    @Test
    fun `is checkmated`() {
        val board = Board(false)
        val kingInCheck = King(BLACK, Position(3, 3))
        board.putPiece(kingInCheck)
        board.putPiece(Rook(WHITE, Position(3, 0)))
        board.putPiece(Rook(WHITE, Position(0, 3)))
        board.putPiece(Queen(WHITE, Position(1, 1)))
        board.putPiece(Bishop(WHITE, Position(5, 1)))
        board.nextTurn()
        println(board)
        assertThat(board.nextTurn).isEqualTo(BLACK)
        assertThat(kingInCheck.inCheck(board)).isTrue()
        assertThat(board.checkmated).isTrue()
    }

    @Test
    fun `is checkmated 2`() {
        val board = Board(false)
        board.putPiece(King(BLACK, Position(0, 0)))
        board.putPiece(Queen(WHITE, Position(2, 0)))
        board.putPiece(Rook(WHITE, Position(0, 5)))
        board.nextTurn()
        assertThat(board.nextTurn).isEqualTo(BLACK)
        assertThat(board.checkmated).isTrue()
    }

    @Test
    fun `is checkmated but attacking piece can be captured`() {
        val board = Board(false)
        board.putPiece(King(BLACK, Position(0, 0)))
        board.putPiece(Rook(WHITE, Position(0, 3)))
        board.putPiece(Rook(WHITE, Position(1, 3)))
        board.putPiece(Rook(BLACK, Position(0, 4)))
        board.nextTurn()
        assertThat(board.nextTurn).isEqualTo(BLACK)
        assertThat(board.checkmated).isFalse()
    }
}

    @Test
    fun `is checkmated but king can capture attacker`() {
        val board = Board(false)
        board.putPiece(King(BLACK, Position(0, 0)))
        board.putPiece(Rook(WHITE, Position(0, 1)))
        board.nextTurn()
        assertThat(board.nextTurn).isEqualTo(BLACK)
        assertThat(board.checkmated).isFalse()
    }
