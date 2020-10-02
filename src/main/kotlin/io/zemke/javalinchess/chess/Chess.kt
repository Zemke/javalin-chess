package io.zemke.javalinchess.chess

import io.zemke.javalinchess.figure.*


object Chess {

    val starting: List<List<Figure>> = listOf(
            listOf(Rook(), Knight(), Bishop(), Queen(), King(), Bishop(), Knight(), Rook()),
            listOf(Pawn(), Pawn(), Pawn(), Pawn(), Pawn(), Pawn(), Pawn(), Pawn()),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf(Pawn(), Pawn(), Pawn(), Pawn(), Pawn(), Pawn(), Pawn(), Pawn()),
            listOf(Rook(), Knight(), Bishop(), Queen(), King(), Bishop(), Knight(), Rook())
    )
}

