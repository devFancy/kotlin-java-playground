package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class RateSummary(
    val rate: BigDecimal,
    val count: Long,
) {
    companion object {
        val EMPTY: RateSummary = RateSummary(rate = BigDecimal.ZERO, count = 0)
    }
}
