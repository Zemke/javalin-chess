package io.zemke.javalinchess.view.controller

import io.javalin.http.Context
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.junit5.MockKExtension
import io.zemke.javalinchess.DelegationContext
import io.zemke.javalinchess.chess.Board
import io.zemke.javalinchess.complex.Memcached
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class BoardControllerTest {

    @OverrideMockKs
    private var boardController = BoardController()

    @MockK
    private lateinit var memcached: Memcached

    @Test
    fun `POST creates valid board`() {
        val ctx = mockk<Context>(relaxed = true)
        mockkConstructor(DelegationContext::class)
        every { anyConstructed<DelegationContext>().header("auth") } returns UUID.randomUUID().toString()
        val boardIdSlot = slot<String>()
        val boardSlot = slot<Board>()
        val resJsonSlot = slot<Board>()
        every { memcached.store(capture(boardIdSlot), capture(boardSlot)) } returns true
        every { anyConstructed<DelegationContext>().json(capture(resJsonSlot))} returns ctx
        boardController.create(ctx)
        assertThat(boardIdSlot.captured).isEqualTo(resJsonSlot.captured.id)
        assertThat(boardSlot.captured).isEqualTo(resJsonSlot.captured)
        assertThat(boardSlot.captured).isEqualTo(Board(true))
        verify { ctx.status(201) }
    }
}
