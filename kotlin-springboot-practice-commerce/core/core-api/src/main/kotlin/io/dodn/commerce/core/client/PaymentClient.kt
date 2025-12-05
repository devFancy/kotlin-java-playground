package io.dodn.commerce.core.client

import io.dodn.commerce.core.domain.PaymentCommand
import io.dodn.commerce.core.domain.PaymentResult

interface PaymentClient {
    fun requestPayment(command: PaymentCommand): PaymentResult
}