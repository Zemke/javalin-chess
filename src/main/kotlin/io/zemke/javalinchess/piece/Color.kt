package io.zemke.javalinchess.piece

enum class Color {
    WHITE, BLACK;

    fun other(): Color = if (this === WHITE) BLACK else WHITE
}
