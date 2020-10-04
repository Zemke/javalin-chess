package io.zemke.javalinchess.controller

import io.zemke.javalinchess.figure.*

class Board {

    val grid: List<List<Figure>>

    constructor(grid: List<List<Figure>>) {
        this.grid = grid
    }

    constructor(p1: Player, p2: Player) {
        grid = listOf(
                listOf(Rook(p1, Color.BLACK, Position(0, 7)), Knight(p1, Color.BLACK, Position(1, 7)), Bishop(p1, Color.BLACK, Position(2, 7)), Queen(p1, Color.BLACK, Position(3, 7)), King(p1, Color.BLACK, Position(4, 7)), Bishop(p1, Color.BLACK, Position(5, 7)), Knight(p1, Color.BLACK, Position(6, 7)), Rook(p1, Color.BLACK, Position(7, 7))),
                listOf(Pawn(p1, Color.BLACK, Position(0, 6)), Pawn(p1, Color.BLACK, Position(1, 6)), Pawn(p1, Color.BLACK, Position(2, 6)), Pawn(p1, Color.BLACK, Position(3, 6)), Pawn(p1, Color.BLACK, Position(4, 6)), Pawn(p1, Color.BLACK, Position(5, 6)), Pawn(p1, Color.BLACK, Position(6, 6)), Pawn(p1, Color.BLACK, Position(7, 6))),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(Pawn(p2, Color.WHITE, Position(0, 1)), Pawn(p2, Color.WHITE, Position(1, 1)), Pawn(p2, Color.WHITE, Position(2, 1)), Pawn(p2, Color.WHITE, Position(3, 1)), Pawn(p2, Color.WHITE, Position(4, 1)), Pawn(p2, Color.WHITE, Position(5, 1)), Pawn(p2, Color.WHITE, Position(6, 1)), Pawn(p2, Color.WHITE, Position(7, 1))),
                listOf(Rook(p2, Color.WHITE, Position(0, 0)), Knight(p2, Color.WHITE, Position(1, 0)), Bishop(p2, Color.WHITE, Position(2, 0)), Queen(p2, Color.WHITE, Position(3, 0)), King(p2, Color.WHITE, Position(4, 0)), Bishop(p2, Color.WHITE, Position(5, 0)), Knight(p2, Color.WHITE, Position(6, 0)), Rook(p2, Color.WHITE, Position(7, 0)))
        )
    }

    fun findFigureById(figureId: String): Figure? =
            grid.flatten().find { figure -> figure.id == figureId }
}
