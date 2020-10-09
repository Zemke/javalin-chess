package io.zemke.javalinchess.controller

import io.zemke.javalinchess.piece.Color

class Match(
        val board: Board,
        val nextTurn: Color,
) : Entity()
