package io.zemke.javalinchess.view

import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.Color

object ViewUtil {

    fun isAuth(board: Board, auth: String?) =
            (board.nextTurn == Color.BLACK && (auth == board.uuidBlack || board.uuidBlack == null)
                    || board.nextTurn == Color.WHITE && (auth == board.uuidWhite || board.uuidWhite == null))
}
