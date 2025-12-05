package io.dodn.commerce.core.facade

import io.dodn.commerce.core.client.PaymentClient
import io.dodn.commerce.core.domain.PaymentService
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PaymentConfirmFacade (
        private val paymentService: PaymentService,
        private val paymentClient: PaymentClient,
){
    fun success(orderKey: String, externalPaymentKey: String, amount: BigDecimal): Long {
        // 1. [DB Transaction] 결제 검증
        val command = paymentService.validatePayment(orderKey, externalPaymentKey, amount)

        // 2. [No Transaction] 외부 API 호출
        // 실패 시 예외가 발생하여 아래 로직 실행 안 됨
        val paymentResult = paymentClient.requestPayment(command)

        // 3. [DB Transaction] 결제 완료 처리
        // 외부 API에서 받은 결과(transactionId 등)를 넘겨줌
        return paymentService.completePayment(orderKey, paymentResult)
    }

    // NOTE: PG사로부터 실패 콜백이 왔을 때 호출됨.
    // - 추후 실패 알림(Slack, Email) 등의 외부 연동 로직을 여기에 추가할 수 있음.
    fun fail(orderKey: String, code: String, message: String) {
        paymentService.fail(orderKey, code, message)
    }
}