package dev.be.circuit.controller

import dev.be.circuit.circuitbreaker.CircuitBreaker
import dev.be.circuit.domain.User
import dev.be.circuit.circuitbreaker.fallback
import dev.be.circuit.circuitbreaker.fallbackIfOpen
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CircuitUtilController(
    private val circuitBreaker: CircuitBreaker
) {

    @GetMapping("/util/users/fallback")
    fun getFallbackSampleUser(): User {
        return circuitBreaker.run ("fallback-user") {
            throw RuntimeException("runtime")
        }.fallback {
            User("Fallback name", "Fallback introduce")
        }.getOrThrow()
    }

    @GetMapping("/util/users/open")
    fun getFallbackOpenSampleUser(): User {
        return circuitBreaker.run("fallback-open-user") {
            throw RuntimeException("runtime")
        }.fallbackIfOpen {
            User("Fallback Open Default name", "Fallback Open Default introduce")
        }.getOrThrow()
    }
}