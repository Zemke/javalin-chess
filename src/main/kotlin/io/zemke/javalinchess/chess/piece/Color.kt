package io.zemke.javalinchess.chess.piece

enum class Color {
    WHITE, BLACK;

    fun other(): Color = if (this === WHITE) BLACK else WHITE
}
