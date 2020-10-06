package io.zemke.javalinchess.piece

import io.zemke.javalinchess.controller.Board
import io.zemke.javalinchess.controller.Player

class Pawn(player: Player, color: Color, position: Position) : Piece("Pawn", player, color, position) {
    override fun move(board: Board, target: Position): Board {
        TODO("Not yet implemented")
    }
}
