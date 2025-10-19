package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class Price(
    val costPrice: BigDecimal,
    val salesPrice: BigDecimal,
    val discountedPrice: BigDecimal,
)
