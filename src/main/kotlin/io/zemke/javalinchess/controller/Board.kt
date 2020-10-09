package io.zemke.javalinchess.controller

import io.zemke.javalinchess.piece.*
import io.zemke.javalinchess.piece.Color.BLACK
import io.zemke.javalinchess.piece.Color.WHITE

class Board {

    private val grid: List<MutableList<Piece?>>

    constructor(grid: List<MutableList<Piece?>>) {
        this.grid = grid
    }

    constructor(init: Boolean) {
        if (init) {
            grid = listOf<MutableList<Piece?>>(
                    mutableListOf(Rook(BLACK, Position(0, 7)), Knight(BLACK, Position(1, 7)), Bishop(BLACK, Position(2, 7)), Queen(BLACK, Position(3, 7)), King(BLACK, Position(4, 7)), Bishop(BLACK, Position(5, 7)), Knight(BLACK, Position(6, 7)), Rook(BLACK, Position(7, 7))),
                    mutableListOf(Pawn(BLACK, Position(0, 6)), Pawn(BLACK, Position(1, 6)), Pawn(BLACK, Position(2, 6)), Pawn(BLACK, Position(3, 6)), Pawn(BLACK, Position(4, 6)), Pawn(BLACK, Position(5, 6)), Pawn(BLACK, Position(6, 6)), Pawn(BLACK, Position(7, 6))),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(Pawn(WHITE, Position(0, 1)), Pawn(WHITE, Position(1, 1)), Pawn(WHITE, Position(2, 1)), Pawn(WHITE, Position(3, 1)), Pawn(WHITE, Position(4, 1)), Pawn(WHITE, Position(5, 1)), Pawn(WHITE, Position(6, 1)), Pawn(WHITE, Position(7, 1))),
                    mutableListOf(Rook(WHITE, Position(0, 0)), Knight(WHITE, Position(1, 0)), Bishop(WHITE, Position(2, 0)), Queen(WHITE, Position(3, 0)), King(WHITE, Position(4, 0)), Bishop(WHITE, Position(5, 0)), Knight(WHITE, Position(6, 0)), Rook(WHITE, Position (7, 0)))
            )

        } else {
            grid = listOf<MutableList<Piece?>>(
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null)
            )
        }
    }

    fun findPieceById(pieceId: String): Piece? =
            grid.flatten()
                    .filterNotNull()
                    .find { it.id == pieceId }

    fun findPositionOfPiece(piece: Piece): Position? {
        grid.forEachIndexed { rankIdx, rank ->
            rank.forEachIndexed { idx, f ->
                if (f == piece) {
                    return@findPositionOfPiece Position(idx, rankIdx)
                }
            }
        }
        return null
    }

    fun move(piece: Piece, target: Position): Board {
        val current = findPositionOfPiece(piece)
                ?: throw RuntimeException("Position of Piece $this not found")
        grid[target.rank][target.file] = piece
        grid[current.rank][current.file] = null
        return this
    }

    fun getPieceAt(position: Position): Piece? {
        return grid[position.rank][position.file]
    }

    fun putPiece(piece: Piece) {
        if (grid.flatten().contains(piece)) {
            throw IllegalArgumentException("Piece $piece is already on the Board.")
        }
        if (getPieceAt(piece.position) != null) {
            throw IllegalArgumentException("Position ${piece.position} is already occupied.")
        }
        grid[piece.position.rank][piece.position.file] = piece
    }

    fun ownPieces(color: Color): List<Piece> {
        return grid.flatten()
                .filterNotNull()
                .filter { it.color == color }
    }

    fun opponentPieces(color: Color): List<Piece> {
        return grid.flatten()
                .filterNotNull()
                .filter { it.color != color }
    }

    override fun toString(): String {
        val nameOnBoard: (Piece?) -> String = {
            it?.let { "${it.color.name.substring(0, 1).toLowerCase()}${it.name}" } ?: ""
        }
        val maxNameLength = grid.flatten()
                .filterNotNull()
                .map { it.name.length + 1 }
                .maxOrNull() ?: 0
        val prefix = (-1 until (grid[0].size)).toList()
                .joinToString(postfix = "\n", separator = " | ") { "$it".padStart(maxNameLength) }
        return "file by rank, x by y\n" + grid.indices.joinToString(separator = "\n", prefix = prefix) { rankIdx ->
            (-1 until grid[rankIdx].size)
                    .map { if (it == -1) "$rankIdx".padStart(maxNameLength) else nameOnBoard(grid[rankIdx][it]) }
                    .joinToString(" | ") { it.padEnd(maxNameLength) }
        }
    }
}
