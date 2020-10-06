package io.zemke.javalinchess.controller

import io.zemke.javalinchess.piece.Color
import io.zemke.javalinchess.piece.Position
import io.zemke.javalinchess.piece.Rook
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BoardTest {

    @Test
    fun `rook move right`() {
        val board = Board()
        val source = Position(3, 3)
        board.putPiece(Rook(Player("p1"), Color.BLACK, source))
        val piece = board.getPieceAt(source)
        Assertions.assertThat(piece).isNotNull
        Assertions.assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(3, 5)
        val actual = board.move(piece!!, target)
        Assertions.assertThat(actual.getPieceAt(target)).isEqualTo(piece)
        Assertions.assertThat(actual.getPieceAt(source)).isNull()
    }

    @Test
    fun `rook move left`() {
        val board = Board()
        val source = Position(3, 3)
        board.putPiece(Rook(Player("p1"), Color.BLACK, source))
        val piece = board.getPieceAt(source)
        Assertions.assertThat(piece).isNotNull
        Assertions.assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(3, 1)
        val actual = board.move(piece!!, target)
        Assertions.assertThat(actual.getPieceAt(target)).isEqualTo(piece)
        Assertions.assertThat(actual.getPieceAt(source)).isNull()
    }

    @Test
    fun `rook move up`() {
        val board = Board()
        val source = Position(3, 3)
        board.putPiece(Rook(Player("p1"), Color.BLACK, source))
        val piece = board.getPieceAt(source)
        Assertions.assertThat(piece).isNotNull
        Assertions.assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(5, 3)
        val actual = board.move(piece!!, target)
        Assertions.assertThat(actual.getPieceAt(target)).isEqualTo(piece)
        Assertions.assertThat(actual.getPieceAt(source)).isNull()
    }

    @Test
    fun `rook move down`() {
        val board = Board()
        val source = Position(3, 3)
        board.putPiece(Rook(Player("p1"), Color.BLACK, source))
        val piece = board.getPieceAt(source)
        Assertions.assertThat(piece).isNotNull
        Assertions.assertThat(piece).isExactlyInstanceOf(Rook::class.java)
        val target = Position(1, 3)
        val actual = board.move(piece!!, target)
        Assertions.assertThat(actual.getPieceAt(target)).isEqualTo(piece)
        Assertions.assertThat(actual.getPieceAt(source)).isNull()
    }
}
