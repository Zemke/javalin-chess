package io.zemke.javalinchess.controller

import io.zemke.javalinchess.figure.Figure

class Match(
        val grid: List<List<Figure>>,
        val nextTurn: Player,
        val player1: Player,
        val player2: Player
) : Entity()
