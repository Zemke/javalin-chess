package io.zemke.javalinchess.view.model

import io.zemke.javalinchess.chess.piece.Position


class TurnDto(
        val piece: String,
        val target: Position
)
