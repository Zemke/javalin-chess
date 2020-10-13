package io.zemke.javalinchess.chess

import io.zemke.javalinchess.chess.piece.Color
import io.zemke.javalinchess.complex.Entity

class Match(
        val board: Board,
        val nextTurn: Color,
) : Entity()
