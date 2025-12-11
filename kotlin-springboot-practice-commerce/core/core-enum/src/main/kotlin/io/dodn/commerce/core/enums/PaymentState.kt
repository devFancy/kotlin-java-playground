package io.dodn.commerce.core.enums

enum class PaymentState {
    READY, // 결제 초기 생성
    IN_PROGRESS, // 결제수단 정보와 해당 결제수단의 소유자가 맞는지 인증을 마친 상태
    SUCCESS, // 결제가 승인된 상태
}
