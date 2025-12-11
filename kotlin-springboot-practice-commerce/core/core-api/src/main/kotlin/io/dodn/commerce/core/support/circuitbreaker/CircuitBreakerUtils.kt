package io.dodn.commerce.core.support.circuitbreaker

import io.dodn.commerce.core.support.error.CircuitOpenException
import io.github.resilience4j.circuitbreaker.CallNotPermittedException

// Result<T> 확장 함수: 실패 시 무조건 Fallback 실행
fun <T> Result<T>.fallback(function: (e: Throwable?) -> T): Result<T> = when (this.isSuccess) {
    true -> this
    false -> kotlin.runCatching { function(this.exceptionOrNull()) }
}

// NOTE: Result<T> 확장 함수: 서킷이 열린 경우(CircuitOpenException)에만 Fallback 실행
fun <T> Result<T>.fallbackIfOpen(function: (e: Throwable?) -> T): Result<T> = when (this.exceptionOrNull()) {
    is CircuitOpenException -> runCatching { function(this.exceptionOrNull()) }
    else -> this
}

// NOTE: Resilience4j 예외를 커스텀 예외로 변환
fun Throwable.convertToCustomException(): Throwable = when (this) {
    is CallNotPermittedException -> CircuitOpenException()
    else -> this
}
