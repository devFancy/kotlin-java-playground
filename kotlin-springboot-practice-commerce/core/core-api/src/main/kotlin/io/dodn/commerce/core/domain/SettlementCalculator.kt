package io.dodn.commerce.core.domain

import java.math.BigDecimal
import java.math.RoundingMode

object SettlementCalculator {
    private val FEE: BigDecimal = BigDecimal.valueOf(0.1)

    fun calculate(amount: BigDecimal): SettlementAmount {
        val feeAmount = amount.multiply(FEE).setScale(2, RoundingMode.HALF_UP)

        return SettlementAmount(
            originalAmount = amount,
            feeAmount = feeAmount,
            feeRate = FEE,
            settlementAmount = amount.subtract(feeAmount).setScale(2, RoundingMode.HALF_UP),
        )
    }
}
