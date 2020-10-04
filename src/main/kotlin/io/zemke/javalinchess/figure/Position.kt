package io.zemke.javalinchess.figure

class Position(val file: Int, val rank: Int) {

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
}
