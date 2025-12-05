package dev.be.circuit

import dev.be.circuit.circuitbreaker.CircuitOpenException
import dev.be.circuit.circuitbreaker.convertToCustomException
import dev.be.circuit.circuitbreaker.fallback
import dev.be.circuit.circuitbreaker.fallbackIfOpen
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CircuitBreakerUtilsTest {

    @Test
    fun `fallback은 실패 시 대체 값을 반환한다`() {
        // given
        val failureResult = Result.failure<String>(RuntimeException("Error"))

        // when
        val result = failureResult.fallback { "Recovered" }

        // then
        assertThat(result.getOrNull()).isEqualTo("Recovered")
    }

    @Test
    fun `fallbackIfOpen은 CircuitOpenException일 때만 대체 값을 반환한다`() {
        // given: 서킷 오픈 예외 발생
        val circuitOpenResult = Result.failure<String>(CircuitOpenException())

        // when
        val result = circuitOpenResult.fallbackIfOpen { "Recovered from Open" }

        // then
        assertThat(result.getOrNull()).isEqualTo("Recovered from Open")
    }

    @Test
    fun `fallbackIfOpen은 일반 예외일 때는 무시하고 에러를 유지한다`() {
        // given: 일반 런타임 예외
        val runtimeError = RuntimeException("General Error")
        val failureResult = Result.failure<String>(runtimeError)
        var isFallbackExecuted = false

        // when
        val result = failureResult.fallbackIfOpen {
            isFallbackExecuted = true
            "Should not happen"
        }

        // then
        // 결과가 여전히 실패인지 확인
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(runtimeError)
        // fallback 블록 내부가 실행되지 않았는지 확인
        assertThat(isFallbackExecuted).isFalse()
    }

    @Test
    fun `Resilience4j 예외가 커스텀 예외로 잘 변환된다`() {
        // given
        val r4jException = mockk<CallNotPermittedException>()
        every { r4jException.fillInStackTrace() } returns r4jException

        // when
        val converted = r4jException.convertToCustomException()

        // then
        assertThat(converted).isInstanceOf(CircuitOpenException::class.java)
    }
}