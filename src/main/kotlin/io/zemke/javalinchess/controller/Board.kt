package io.zemke.javalinchess.controller

import io.zemke.javalinchess.piece.*
import io.zemke.javalinchess.piece.Color.BLACK
import io.zemke.javalinchess.piece.Color.WHITE

class Board {

    private val grid: List<MutableList<Piece?>>

    constructor(grid: List<MutableList<Piece?>>) {
        this.grid = grid
    }

    constructor(p1: Player, p2: Player) {
        grid = listOf<MutableList<Piece?>>(
                mutableListOf(Rook(p1, BLACK, Position(0, 7)), Knight(p1, BLACK, Position(1, 7)), Bishop(p1, BLACK, Position(2, 7)), Queen(p1, BLACK, Position(3, 7)), King(p1, BLACK, Position(4, 7)), Bishop(p1, BLACK, Position(5, 7)), Knight(p1, BLACK, Position(6, 7)), Rook(p1, BLACK, Position(7, 7))),
                mutableListOf(Pawn(p1, BLACK, Position(0, 6)), Pawn(p1, BLACK, Position(1, 6)), Pawn(p1, BLACK, Position(2, 6)), Pawn(p1, BLACK, Position(3, 6)), Pawn(p1, BLACK, Position(4, 6)), Pawn(p1, BLACK, Position(5, 6)), Pawn(p1, BLACK, Position(6, 6)), Pawn(p1, BLACK, Position(7, 6))),
                mutableListOf(null, null, null, null, null, null, null, null),
                mutableListOf(null, null, null, null, null, null, null, null),
                mutableListOf(null, null, null, null, null, null, null, null),
                mutableListOf(null, null, null, null, null, null, null, null),
                mutableListOf(Pawn(p2, WHITE, Position(0, 1)), Pawn(p2, WHITE, Position(1, 1)), Pawn(p2, WHITE, Position(2, 1)), Pawn(p2, WHITE, Position(3, 1)), Pawn(p2, WHITE, Position(4, 1)), Pawn(p2, WHITE, Position(5, 1)), Pawn(p2, WHITE, Position(6, 1)), Pawn(p2, WHITE, Position(7, 1))),
                mutableListOf(Rook(p2, WHITE, Position(0, 0)), Knight(p2, WHITE, Position(1, 0)), Bishop(p2, WHITE, Position(2, 0)), Queen(p2, WHITE, Position(3, 0)), King(p2, WHITE, Position(4, 0)), Bishop(p2, WHITE, Position(5, 0)), Knight(p2, WHITE, Position(6, 0)), Rook(p2, WHITE, Position(7, 0)))
        )
    }

    constructor() {
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
        grid[piece.position.rank][piece.position.file] = piece
    }

    override fun toString(): String {
        val maxNameLength = grid.flatten()
                .filterNotNull()
                .map { it.name.length }
                .maxOrNull() ?: 0
        return grid.joinToString(separator = "\n") { rank ->
            rank.map { it?.name ?: "" }.joinToString(" | ") { it.padEnd(maxNameLength) }
        }
    }
}
