package io.zemke.javalinchess.controller

class Match(
        val board: Board,
        val nextTurn: Player,
        val player1: Player,
        val player2: Player
) : Entity()
