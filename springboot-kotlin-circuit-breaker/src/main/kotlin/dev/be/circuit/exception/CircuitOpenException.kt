package dev.be.circuit.exception

class CircuitOpenException(message: String = "Circuit breaker is open") : RuntimeException(message)