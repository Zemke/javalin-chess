package io.zemke.javalinchess.figure

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RookTest {

    @Test
    fun `move right`() {
        val board = Board()
        val source = Position(3, 3)
        board.putFigure(Rook(Player("p1"), Color.BLACK, source))
        val figure = board.getFigureAt(source)
        assertThat(figure).isNotNull
        assertThat(figure).isExactlyInstanceOf(Rook::class.java)
        val target = Position(3, 5)
        val actual = figure!!.move(board, target)
        assertThat(actual.getFigureAt(target)).isEqualTo(figure)
        assertThat(actual.getFigureAt(source)).isNull()
    }

    @Test
    fun `move left`() {
        val board = Board()
        val source = Position(3, 3)
        board.putFigure(Rook(Player("p1"), Color.BLACK, source))
        val figure = board.getFigureAt(source)
        assertThat(figure).isNotNull
        assertThat(figure).isExactlyInstanceOf(Rook::class.java)
        val target = Position(3, 1)
        val actual = figure!!.move(board, target)
        assertThat(actual.getFigureAt(target)).isEqualTo(figure)
        assertThat(actual.getFigureAt(source)).isNull()
    }

    @Test
    fun `move up`() {
        val board = Board()
        val source = Position(3, 3)
        board.putFigure(Rook(Player("p1"), Color.BLACK, source))
        val figure = board.getFigureAt(source)
        assertThat(figure).isNotNull
        assertThat(figure).isExactlyInstanceOf(Rook::class.java)
        val target = Position(5, 3)
        val actual = figure!!.move(board, target)
        assertThat(actual.getFigureAt(target)).isEqualTo(figure)
        assertThat(actual.getFigureAt(source)).isNull()
    }

    @Test
    fun `move down`() {
        val board = Board()
        val source = Position(3, 3)
        board.putFigure(Rook(Player("p1"), Color.BLACK, source))
        val figure = board.getFigureAt(source)
        assertThat(figure).isNotNull
        assertThat(figure).isExactlyInstanceOf(Rook::class.java)
        val target = Position(1, 3)
        val actual = figure!!.move(board, target)
        assertThat(actual.getFigureAt(target)).isEqualTo(figure)
        assertThat(actual.getFigureAt(source)).isNull()
    }
}

