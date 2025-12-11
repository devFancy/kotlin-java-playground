package io.dodn.commerce.core.support.error

class CircuitOpenException(message: String = "Circuit breaker is open") : RuntimeException(message)
