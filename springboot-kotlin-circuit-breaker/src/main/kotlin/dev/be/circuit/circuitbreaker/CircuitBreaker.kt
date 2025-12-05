package dev.be.circuit.circuitbreaker

import org.slf4j.LoggerFactory
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.stereotype.Component

interface CircuitBreaker {
    /**
     * @param name 서킷 브레이커 설정 이름 (기본값: "default")
     * @param block 실행할 로직
     */
    fun <T> run(name: String = "default", block: () -> T): Result<T>
}

@Component
class DefaultCircuitBreaker(
    private val factory: CircuitBreakerFactory<*, *>
) : CircuitBreaker {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun <T> run(
        name: String,
        block: () -> T
    ): Result<T> {
        val circuitBreaker = factory.create(name)

        return kotlin.runCatching {
            circuitBreaker.run(block) { e ->
                log.warn("[CircuitBreaker: $name] 실행 중 예외 발생: ${e.message}", e)
                throw e.convertToCustomException()
            }
        }
    }
}

class CircuitOpenException(message: String = "Circuit breaker is open") : RuntimeException(message)