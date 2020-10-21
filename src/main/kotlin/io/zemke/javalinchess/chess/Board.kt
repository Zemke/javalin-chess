package io.zemke.javalinchess.chess

import io.zemke.javalinchess.chess.piece.*
import io.zemke.javalinchess.chess.piece.Color.BLACK
import io.zemke.javalinchess.chess.piece.Color.WHITE
import io.zemke.javalinchess.complex.Entity

class Board : Entity {

    val grid: List<MutableList<Piece?>>
    val movements: MutableList<Pair<Piece, Position>> = mutableListOf()
    var nextTurn: Color = WHITE
        private set
    var uuidWhite: String? = null
    var uuidBlack: String? = null

    constructor(grid: List<MutableList<Piece?>>) {
        this.grid = grid
    }

    constructor(init: Boolean) {
        if (init) {
            grid = listOf<MutableList<Piece?>>(
                    mutableListOf(Rook(BLACK, Position(0, 0)), Knight(BLACK, Position(1, 0)), Bishop(BLACK, Position(2, 0)), Queen(BLACK, Position(3, 0)), King(BLACK, Position(4, 0)), Bishop(BLACK, Position(5, 0)), Knight(BLACK, Position(6, 0)), Rook(BLACK, Position (7, 0))),
                    mutableListOf(Pawn(BLACK, Position(0, 1)), Pawn(BLACK, Position(1, 1)), Pawn(BLACK, Position(2, 1)), Pawn(BLACK, Position(3, 1)), Pawn(BLACK, Position(4, 1)), Pawn(BLACK, Position(5, 1)), Pawn(BLACK, Position(6, 1)), Pawn(BLACK, Position(7, 1))),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(null, null, null, null, null, null, null, null),
                    mutableListOf(Pawn(WHITE, Position(0, 6)), Pawn(WHITE, Position(1, 6)), Pawn(WHITE, Position(2, 6)), Pawn(WHITE, Position(3, 6)), Pawn(WHITE, Position(4, 6)), Pawn(WHITE, Position(5, 6)), Pawn(WHITE, Position(6, 6)), Pawn(WHITE, Position(7, 6))),
                    mutableListOf(Rook(WHITE, Position(0, 7)), Knight(WHITE, Position(1, 7)), Bishop(WHITE, Position(2, 7)), Queen(WHITE, Position(3, 7)), King(WHITE, Position(4, 7)), Bishop(WHITE, Position(5, 7)), Knight(WHITE, Position(6, 7)), Rook(WHITE, Position(7, 7)))
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

    constructor(board: Board) {
        grid = board.grid.map { mutableListOf(*it.toTypedArray()) }
    }

    fun findPieceById(pieceId: String): Piece? =
            grid.flatten()
                    .filterNotNull()
                    .find { it.id == pieceId }

    /**
     * @throws RuntimeException When the Piece could not be found on the Board.
     */
    fun findPositionOfPiece(piece: Piece): Position {
        grid.forEachIndexed { rankIdx, rank ->
            rank.forEachIndexed { idx, f ->
                if (f == piece) {
                    return@findPositionOfPiece Position(idx, rankIdx)
                }
            }
        }
        throw RuntimeException("Position of Piece $piece not found")
    }

    fun move(piece: Piece, target: Position): Board {
        val current = findPositionOfPiece(piece)
        if (piece is Pawn && getPieceAt(target) == null && isPassible(Position(target.file, current.rank))) {
            grid[current.rank][target.file] = null
        }
        grid[target.rank][target.file] = piece
        grid[current.rank][current.file] = null
        movements.add(piece to target)
        return this
    }

    fun nextTurn() {
        this.nextTurn = this.nextTurn.other()
    }

    fun getPieceAt(position: Position): Piece? {
        return grid[position.rank][position.file]
    }

    fun putPiece(piece: Piece, force: Boolean = false) {
        if (!force) {
            if (grid.flatten().contains(piece)) {
                throw IllegalArgumentException("Piece $piece is already on the Board.")
            }
            if (getPieceAt(piece.position) != null) {
                throw IllegalArgumentException("Position ${piece.position} is already occupied.")
            }
        }
        grid[piece.position.rank][piece.position.file] = piece
    }

    fun putPieces(vararg pieces: Piece, force: Boolean = false) {
        pieces.forEach { putPiece(it, force) }
    }

    fun ownPieces(color: Color): List<Piece> {
        return grid.flatten()
                .filterNotNull()
                .filter { it.isOwn(color) }
    }

    fun opponentPieces(color: Color): List<Piece> {
        return grid.flatten()
                .filterNotNull()
                .filter { it.color != color }
    }

    /** en passant */
    fun isPassible(position: Position): Boolean {
        val piece = getPieceAt(position)
        if (piece is Pawn && movements.isNotEmpty() && movements.last().first == piece) {
            val (from, to) = findMovements(piece).last()
            return piece.isLongLeap(from, to)
        }
        return false
    }

    fun findMovements(piece: Piece): List<Pair<Position, Position>> {
        val movements = movements
                .filter { it.first === piece }
                .map { it.second }
                .toMutableList()
        if (movements.isNotEmpty()) {
            movements.add(0, piece.position)
        }
        return movements.zipWithNext()
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (grid != other.grid) return false

        return true
    }

    override fun hashCode(): Int {
        return grid.hashCode()
    }
}
