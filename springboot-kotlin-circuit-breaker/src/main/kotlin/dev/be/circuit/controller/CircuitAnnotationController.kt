package dev.be.circuit.controller;

import dev.be.circuit.domain.User
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController;

@RestController
class CircuitAnnotationController() {

    // fallback은 본 함수와 인자가 일치해야함.
    @CircuitBreaker(name = "user", fallbackMethod = "failSample")
    @GetMapping("/annotation/user/{id}")
    fun getSampleUser(@PathVariable id: String): User {
        val list = listOf(
                IllegalStateException("illegalState"),
                IllegalArgumentException("illegalArgument"),
                )
        throw list.random()
    }

    private fun failSample(throwable: IllegalArgumentException): User {
        return User("IllegalArgumentException name", "IllegalArgumentExceptionfail introduce")
    }

    private fun failSample(e: IllegalStateException): User {
        return User("IllegalStateException name", "IllegalStateExceptionfail introduce")
    }
}
