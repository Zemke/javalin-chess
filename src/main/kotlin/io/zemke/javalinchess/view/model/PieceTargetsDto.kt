package io.zemke.javalinchess.view.model

import java.io.Serializable
import io.zemke.javalinchess.chess.piece.Position
import io.zemke.javalinchess.chess.piece.Piece

class PieceTargetsDto(val piece: Piece, val targets: Set<Position>) : Serializable

