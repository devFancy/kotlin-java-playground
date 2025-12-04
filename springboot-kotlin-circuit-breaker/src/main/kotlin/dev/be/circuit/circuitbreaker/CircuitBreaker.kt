package dev.be.circuit.circuitbreaker

import dev.be.circuit.utils.convertToCustomException
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.stereotype.Component

interface CircuitBreaker {
    fun <T> run(name: String, block: () -> T): Result<T>
}

@Component
class DefaultCircuitBreaker(
        private val factory: CircuitBreakerFactory<*, *>
) : CircuitBreaker {

    override fun <T> run(name: String, block: () -> T): Result<T> = kotlin.runCatching {
        factory.create(name).run(block) { e ->
            throw e.convertToCustomException()
        }
    }
}