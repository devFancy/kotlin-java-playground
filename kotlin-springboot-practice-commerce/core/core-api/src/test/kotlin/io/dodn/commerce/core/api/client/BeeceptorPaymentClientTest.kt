package io.dodn.commerce.core.api.client

import io.dodn.commerce.ContextTest
import io.dodn.commerce.core.config.TestRestClientConfig
import io.dodn.commerce.core.domain.PaymentCommand
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Import(TestRestClientConfig::class)
class BeeceptorPaymentClientTest(
    private val beeceptorPaymentClient: BeeceptorPaymentClient,
    private val circuitBreakerRegistry: CircuitBreakerRegistry,
) : ContextTest() {
    @Test
    fun `외부 API가 반복적으로 실패하면 서킷 브레이커가 OPEN 되고 Fallback 예외가 발생한다`() {
        // given
        val command = PaymentCommand(
            externalPaymentKey = "payment_key_test",
            orderId = 1L,
            amount = 10000L,
        )

        val circuitBreaker = circuitBreakerRegistry.circuitBreaker("beeceptor-payment")
        assertThat(circuitBreaker.state).isEqualTo(CircuitBreaker.State.CLOSED)

        // when
        for (i in 1..10) {
            try {
                beeceptorPaymentClient.requestPayment(command)
            } catch (e: Exception) {
            }
        }

        // then
        assertThat(circuitBreaker.state).isEqualTo(CircuitBreaker.State.OPEN)

        val exception = assertThrows(CoreException::class.java) {
            beeceptorPaymentClient.requestPayment(command)
        }
        assertThat(exception.errorType).isEqualTo(ErrorType.PAYMENT_EXTERNAL_API_UNAVAILABLE)
    }
}
