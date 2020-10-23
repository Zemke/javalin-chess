package io.zemke.javalinchess.chess.piece

import io.zemke.javalinchess.chess.Board

class Queen(color: Color, position: Position) : Piece("Queen", color, position) {

    override fun allowedNextPositions(board: Board): Set<Position> {
        val copyBoard = Board(board)
        val current = copyBoard.findPosition(this)
        val rook = Rook(color, current)
        copyBoard.putPiece(rook, true)
        val rookAllowedNextPositions = rook.allowedNextPositions(copyBoard)
        val bishop = Bishop(color, current)
        copyBoard.putPiece(bishop, true)
        val bishopAllowedNextPositions = bishop.allowedNextPositions(copyBoard)
        return setOf(
                *rookAllowedNextPositions.toTypedArray(),
                *bishopAllowedNextPositions.toTypedArray())
    }
}
