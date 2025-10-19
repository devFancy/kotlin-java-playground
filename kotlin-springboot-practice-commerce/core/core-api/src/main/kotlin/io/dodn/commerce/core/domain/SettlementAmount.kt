package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class SettlementAmount(
    val originalAmount: BigDecimal,
    val feeAmount: BigDecimal,
    val feeRate: BigDecimal,
    val settlementAmount: BigDecimal,
)
