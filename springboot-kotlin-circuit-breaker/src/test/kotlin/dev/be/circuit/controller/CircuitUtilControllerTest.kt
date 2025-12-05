package dev.be.circuit.controller

import dev.be.circuit.circuitbreaker.CircuitBreaker
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CircuitUtilControllerTest {

    private val circuitBreaker: CircuitBreaker = mockk()
    private val controller = CircuitUtilController(circuitBreaker)

    @Test
    fun `Controller는 CircuitBreaker가 실패해도 fallback 값을 반환한다`() {
        // given
        // execute가 호출되면 무조건 실패(Result.failure)를 반환하도록 설정
        every {
            circuitBreaker.run<Any>(any(), any())
        } returns Result.failure(RuntimeException("Error"))

        // when
        val user = controller.getFallbackSampleUser()

        // then
        assertThat(user.name).isEqualTo("Fallback name")
    }
}