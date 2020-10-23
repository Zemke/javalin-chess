package io.zemke.javalinchess.view.controller

import io.javalin.http.Context
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import io.zemke.javalinchess.DelegationContext
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.*
import io.zemke.javalinchess.complex.Memcached
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PieceControllerTest {

    @OverrideMockKs
    private lateinit var pieceController: PieceController

    @MockK
    private lateinit var memcached: Memcached

    @Test
    fun allowedNextPositions() {
        val ctx = mockk<Context>(relaxed = true)
        mockkConstructor(DelegationContext::class)
        val board = Board(true)
        val piece = board.findPiece(Position(0, 6))!!
        every { anyConstructed<DelegationContext>().pathParam("key") } returns board.id
        every { anyConstructed<DelegationContext>().pathParam("pieceKey") } returns piece.id
        every { memcached.retrieve<Board>(board.id) } returns board
        pieceController.allowedNextPositions(ctx)
        verify { ctx.status(200) }
        verify { ctx.json(piece.allowedNextPositions(board)) }
    }

    @Test
    fun `allowedNextPositions when in check move wouldn't escape check`() {
        val ctx = mockk<Context>(relaxed = true)
        mockkConstructor(DelegationContext::class)
        val board = Board(false)
        val knight = Knight(Color.WHITE, Position(0, 1))
        board.putPieces(
                knight,
                King(Color.WHITE, Position(3, 3)),
                Queen(Color.BLACK, Position(3, 2)),
                King(Color.BLACK, Position(0, 0)))
        every { anyConstructed<DelegationContext>().pathParam("key") } returns board.id
        every { anyConstructed<DelegationContext>().pathParam("pieceKey") } returns knight.id
        every { memcached.retrieve<Board>(board.id) } returns board
        pieceController.allowedNextPositions(ctx)
        verify { ctx.status(200) }
        verify { ctx.json(emptySet<Position>()) }
    }

    @Test
    fun `allowedNextPositions when in check and move captures attacking opponent piece`() {
        val ctx = mockk<Context>(relaxed = true)
        mockkConstructor(DelegationContext::class)
        val board = Board(false)
        val pawn = Pawn(Color.WHITE, Position(7, 5))
        board.putPieces(
                pawn,
                King(Color.WHITE, Position(5, 5)),
                Pawn(Color.BLACK, Position(6, 4)),
                King(Color.BLACK, Position(0, 0)))
        every { anyConstructed<DelegationContext>().pathParam("key") } returns board.id
        every { anyConstructed<DelegationContext>().pathParam("pieceKey") } returns pawn.id
        every { memcached.retrieve<Board>(board.id) } returns board
        pieceController.allowedNextPositions(ctx)
        verify { ctx.status(200) }
        verify { ctx.json(setOf(Position(6, 4))) }
    }
}
