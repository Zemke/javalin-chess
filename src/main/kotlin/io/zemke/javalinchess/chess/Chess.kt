package io.zemke.javalinchess.chess

import io.zemke.javalinchess.controller.Player
import io.zemke.javalinchess.figure.*
import io.zemke.javalinchess.figure.Color.BLACK
import io.zemke.javalinchess.figure.Color.WHITE

object Chess {

    fun createStartingGrid(p1: Player, p2: Player): List<List<Figure>> {
        return listOf(
                listOf(Rook(p1, BLACK, Position(0, 7)), Knight(p1, BLACK, Position(1, 7)), Bishop(p1, BLACK, Position(2, 7)), Queen(p1, BLACK, Position(3, 7)), King(p1, BLACK, Position(4, 7)), Bishop(p1, BLACK, Position(5, 7)), Knight(p1, BLACK, Position(6, 7)), Rook(p1, BLACK, Position(7, 7))),
                listOf(Pawn(p1, BLACK, Position(0, 6)), Pawn(p1, BLACK, Position(1, 6)), Pawn(p1, BLACK, Position(2, 6)), Pawn(p1, BLACK, Position(3, 6)), Pawn(p1, BLACK, Position(4, 6)), Pawn(p1, BLACK, Position(5, 6)), Pawn(p1, BLACK, Position(6, 6)), Pawn(p1, BLACK, Position(7, 6))),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(Pawn(p2, WHITE, Position(0, 1)), Pawn(p2, WHITE, Position(1, 1)), Pawn(p2, WHITE, Position(2, 1)), Pawn(p2, WHITE, Position(3, 1)), Pawn(p2, WHITE, Position(4, 1)), Pawn(p2, WHITE, Position(5, 1)), Pawn(p2, WHITE, Position(6, 1)), Pawn(p2, WHITE, Position(7, 1))),
                listOf(Rook(p2, WHITE, Position(0, 0)), Knight(p2, WHITE, Position(1, 0)), Bishop(p2, WHITE, Position(2, 0)), Queen(p2, WHITE, Position(3, 0)), King(p2, WHITE, Position(4, 0)), Bishop(p2, WHITE, Position(5, 0)), Knight(p2, WHITE, Position(6, 0)), Rook(p2, WHITE, Position(7, 0)))
        )
    }
}
