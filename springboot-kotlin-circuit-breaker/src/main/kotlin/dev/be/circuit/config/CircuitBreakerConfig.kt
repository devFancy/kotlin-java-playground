package dev.be.circuit.config

import dev.be.circuit.circuitbreaker.CircuitBreaker
import dev.be.circuit.circuitbreaker.CircuitBreakerProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CircuitBreakerConfig {

    @Bean
    fun circuitBreakerProvider(
            circuitBreaker: CircuitBreaker,
    ) = CircuitBreakerProvider(circuitBreaker)
}