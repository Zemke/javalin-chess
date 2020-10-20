package io.zemke.javalinchess.chess.piece

import java.io.Serializable

class Position(val file: Int, val rank: Int) : Serializable {

    init {
        if (!isValidPosition(file, rank))
            throw IllegalArgumentException("Invalid position. (file=$file, rank=$rank)")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (file != other.file) return false
        if (rank != other.rank) return false

        return true
    }

    override fun hashCode(): Int {
        var result = file
        result = 31 * result + rank
        return result
    }

    override fun toString(): String {
        return "Position(file=$file, rank=$rank)"
    }

    operator fun component1(): Int {
        return file
    }

    operator fun component2(): Int {
        return rank
    }

    companion object {
        fun isValidPosition(file: Int, rank: Int): Boolean = rank in 0..7 && file in 0..7

        fun ifValid(file: Int, rank: Int): Position? =
                if (isValidPosition(file, rank)) Position(file, rank) else null
    }
}
