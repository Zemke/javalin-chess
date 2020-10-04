package io.zemke.javalinchess.controller

import io.zemke.javalinchess.figure.*
import io.zemke.javalinchess.figure.Color.BLACK
import io.zemke.javalinchess.figure.Color.WHITE

class Board {

    private val grid: List<MutableList<Figure?>>

    constructor(grid: List<MutableList<Figure?>>) {
        this.grid = grid
    }

    constructor(p1: Player, p2: Player) {
        grid = listOf<MutableList<Figure?>>(
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
        grid = listOf<MutableList<Figure?>>(
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

    fun findFigureById(figureId: String): Figure? =
            grid.flatten()
                    .filterNotNull()
                    .find { figure -> figure.id == figureId }

    fun findPositionOfFigure(figure: Figure): Position? {
        grid.forEachIndexed { rankIdx, rank ->
            rank.forEachIndexed { idx, f ->
                if (f == figure) {
                    return@findPositionOfFigure Position(idx, rankIdx)
                }
            }
        }
        return null
    }

    fun move(figure: Figure, target: Position): Board {
        val current = findPositionOfFigure(figure)
                ?: throw RuntimeException("Position of Figure $this not found")
        grid[target.rank][target.file] = figure
        grid[current.rank][current.file] = null
        return this
    }

    fun getFigureAt(position: Position): Figure? {
        return grid[position.rank][position.file]
    }

    fun putFigure(figure: Figure) {
        grid[figure.position.rank][figure.position.file] = figure
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
