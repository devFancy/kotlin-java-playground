package io.dodn.commerce.core.support.error

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ErrorTypeTest {
    @Test
    fun `ErrorCode_중복_사용_확인`() {
        val codes = ErrorType.entries.map { it.code }
        val duplicates = codes.groupingBy { it }.eachCount().filter { it.value > 1 }.keys

        assertTrue(duplicates.isEmpty(), "중복된 ErrorCode가 있습니다: $duplicates")
    }

    @Test
    fun `ErrorCode가_ErrorType에서_모두_사용되는지_확인`() {
        val declaredCodes = ErrorCode.values().toSet()
        val usedCodes = ErrorType.values().map { it.code }.toSet()

        val unused = declaredCodes - usedCodes

        assertTrue(unused.isEmpty(), "사용되지 않은 ErrorCode가 있습니다: $unused")
    }
}
