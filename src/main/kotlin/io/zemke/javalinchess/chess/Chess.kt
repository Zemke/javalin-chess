package io.zemke.javalinchess.chess

import io.zemke.javalinchess.controller.Player
import io.zemke.javalinchess.figure.*


object Chess {

    fun createStartingGrid(player1: Player, player2: Player): List<List<Figure>> {
        return listOf(
                listOf(Rook(player1), Knight(player1), Bishop(player1), Queen(player1), King(player1), Bishop(player1), Knight(player1), Rook(player1)),
                listOf(Pawn(player1), Pawn(player1), Pawn(player1), Pawn(player1), Pawn(player1), Pawn(player1), Pawn(player1), Pawn(player1)),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(Pawn(player2), Pawn(player2), Pawn(player2), Pawn(player2), Pawn(player2), Pawn(player2), Pawn(player2), Pawn(player2)),
                listOf(Rook(player2), Knight(player2), Bishop(player2), Queen(player2), King(player2), Bishop(player2), Knight(player2), Rook(player2))
        )
    }
}
