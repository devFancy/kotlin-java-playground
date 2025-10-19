package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class ReviewContent(
    val rate: BigDecimal,
    val content: String,
)
