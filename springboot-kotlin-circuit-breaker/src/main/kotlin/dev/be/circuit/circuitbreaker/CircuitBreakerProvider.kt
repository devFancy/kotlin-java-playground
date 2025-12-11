package dev.be.circuit.circuitbreaker

class CircuitBreakerProvider(
        circuitBreaker: CircuitBreaker,
) {
    init {
        Companion.circuitBreaker = circuitBreaker
    }

    companion object {
        private lateinit var circuitBreaker: CircuitBreaker
        fun get() = circuitBreaker
    }
}