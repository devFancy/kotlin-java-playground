package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PaymentMethod

data class PaymentResult(
        val externalPaymentKey: String,     // 외부 결제 키
        val method: PaymentMethod,          // 결제 수단
        val approveNo: String,              // 승인 번호
        val message: String,                // 응답 메시지
)
