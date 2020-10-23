package io.zemke.javalinchess.view.controller

import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJson
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.junit5.MockKExtension
import io.zemke.javalinchess.DelegationContext
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.chess.piece.Color
import io.zemke.javalinchess.chess.piece.Pawn
import io.zemke.javalinchess.chess.piece.Position
import io.zemke.javalinchess.complex.Memcached
import io.zemke.javalinchess.view.model.TurnDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class TurnControllerTest {

    @OverrideMockKs
    private var turnController = TurnController()

    @MockK
    private lateinit var memcached: Memcached

    private val authUuid = UUID.randomUUID().toString()

    @Test
    fun `POST new turn`() {
        val ctx = mockk<Context>(relaxed = true)
        mockkConstructor(DelegationContext::class)
        val board = Board(true)
        board.uuidWhite = authUuid
        val piece = board.findPiece(Position(0, 6)) ?: throw RuntimeException()
        every { memcached.retrieve<Board>(board.id) } returns board
        val target = Position(0, 4)
        val newBoard = Board(true).move(piece, target)
        every { memcached.store(board.id, newBoard) } returns true
        every { anyConstructed<DelegationContext>().header("auth") } returns authUuid
        every { anyConstructed<DelegationContext>().pathParam("key") } returns board.id
        every { anyConstructed<DelegationContext>().body() } returns JavalinJson.toJson(TurnDto(piece.id, target))
        val boardSlot = slot<Board>()
        every { anyConstructed<DelegationContext>().json(capture(boardSlot)) } returns ctx
        turnController.create(ctx)
        assertThat(boardSlot.captured).isEqualTo(newBoard)
        assertThat(boardSlot.captured.nextTurn).isEqualTo(Color.BLACK)
        verify { ctx.status(201) }
    }

    @Test
    fun `POST new turn with promotion`() {
        val ctx = mockk<Context>(relaxed = true)
        mockkConstructor(DelegationContext::class)
        val board = Board(false)
        val pawn = Pawn(Color.BLACK, Position(4, 6))
        board.putPiece(pawn)
        board.nextTurn()
        val target = Position(4, 7)
        val turnDto = TurnDto(piece = pawn.id, target = target, promotion = "Queen")
        every { anyConstructed<DelegationContext>().body() } returns JavalinJson.toJson(turnDto)
        every { anyConstructed<DelegationContext>().header("auth") } returns authUuid
        every { anyConstructed<DelegationContext>().pathParam("key") } returns board.id
        every { anyConstructed<DelegationContext>().pathParam("boardKey") } returns board.id
        every { anyConstructed<DelegationContext>().pathParam("pieceKey") } returns pawn.id
        val boardSlot = slot<Board>()
        every { memcached.retrieve<Board>(board.id) } returns board
        every { memcached.store(board.id, capture(boardSlot)) } returns true
        turnController.create(ctx)
        val promotion = boardSlot.captured.findPiece(target)
        assertThat(promotion).isNotNull
        assertThat(promotion!!.position).isEqualTo(target)
        assertThat(promotion.color).isEqualTo(pawn.color)
    }
}
