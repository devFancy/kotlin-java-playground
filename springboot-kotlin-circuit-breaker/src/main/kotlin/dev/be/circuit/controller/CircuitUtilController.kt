package dev.be.circuit.controller

import dev.be.circuit.domain.User
import dev.be.circuit.utils.circuit
import dev.be.circuit.utils.fallback
import dev.be.circuit.utils.fallbackIfOpen
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CircuitUtilController() {

    @GetMapping("/util/users/fallback")
    fun getFallbackSampleUser(): User {
        return circuit ("fallback-user") {
            throw RuntimeException("runtime")
        }.fallback {
            User("Fallback name", "Fallback introduce")
        }.getOrThrow()
    }

    @GetMapping("/util/users/open")
    fun getFallbackOpenSampleUser(): User {
        return circuit("fallback-open-user") {
            throw RuntimeException("runtime")
        }.fallbackIfOpen {
            User("Fallback Open Default name", "Fallback Open Default introduce")
        }.getOrThrow()
    }
}