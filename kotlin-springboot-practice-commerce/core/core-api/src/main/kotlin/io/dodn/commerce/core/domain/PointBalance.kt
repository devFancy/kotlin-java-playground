package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class PointBalance(
    val userId: Long,
    val balance: BigDecimal,
)
