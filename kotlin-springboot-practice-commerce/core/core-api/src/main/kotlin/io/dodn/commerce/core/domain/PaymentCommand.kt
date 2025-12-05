package io.dodn.commerce.core.domain

data class PaymentCommand(
        val externalPaymentKey: String,
        val orderId: Long,
        val amount: Long
)
