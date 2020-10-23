package io.zemke.javalinchess.view.model

import io.zemke.javalinchess.chess.piece.Position
import java.io.Serializable

class TurnDto(
        val piece: String,
        val target: Position,
        val promotion: String? = null
) : Serializable
