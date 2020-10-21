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
import io.zemke.javalinchess.chess.piece.Position
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
        val piece = board.getPieceAt(Position(0, 6))!!
        every { anyConstructed<DelegationContext>().pathParam("key") } returns board.id
        every { anyConstructed<DelegationContext>().pathParam("pieceId") } returns piece.id
        every { memcached.retrieve<Board>(board.id) } returns board
        pieceController.allowedNextPositions(ctx)
        verify { ctx.status(200) }
        verify { ctx.json(piece.allowedNextPositions(board)) }
    }
}
