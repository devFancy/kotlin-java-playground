package io.dodn.commerce.core.api.client

import io.dodn.commerce.core.client.PaymentClient
import io.dodn.commerce.core.domain.PaymentCommand
import io.dodn.commerce.core.domain.PaymentResult
import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.support.circuitbreaker.CircuitBreaker
import io.dodn.commerce.core.support.circuitbreaker.fallbackIfOpen
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class BeeceptorPaymentClient(
    private val restClient: RestClient,
    private val circuitBreaker: CircuitBreaker,
) : PaymentClient {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun requestPayment(command: PaymentCommand): PaymentResult {
        return circuitBreaker.run("beeceptor-payment") {
            executePayment(command)
        }
            .fallbackIfOpen {
                log.warn("[Circuit Open] Beeceptor 결제 서비스 차단됨. 잠시 후 재시도 필요.")
                throw CoreException(ErrorType.PAYMENT_EXTERNAL_API_UNAVAILABLE)
            }
            .getOrElse { e ->
                when (e) {
                    is HttpClientErrorException -> {
                        log.warn("[PAYMENT_REJECTED] 결제 거절: ${e.responseBodyAsString}")
                        throw CoreException(ErrorType.PAYMENT_REJECTED)
                    }

                    is CoreException -> throw e
                    else -> {
                        log.error("[PAYMENT_FAILED] Beeceptor 결제 호출 실패", e)
                        throw CoreException(ErrorType.PAYMENT_EXTERNAL_API_FAIL)
                    }
                }
            }
    }

    private fun executePayment(command: PaymentCommand): PaymentResult {
        val requestPayload = BeeceptorPaymentRequest(
            paymentKey = command.externalPaymentKey,
            orderId = command.orderId.toString(),
            amount = command.amount,
        )
        log.info("[PAYMENT_CLIENT] Beeceptor 외부 결제 API 호출. Request: {}", requestPayload)

        val response = restClient.post()
            .uri("/v1/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestPayload)
            .retrieve() // 요청 전송 및 응답 수신 시작
            .body<BeeceptorPaymentResponse>()
            ?: throw CoreException(ErrorType.PAYMENT_EXTERNAL_API_BODY_EMPTY)

        val result = PaymentResult(
            externalPaymentKey = response.externalPaymentKey,
            method = PaymentMethod.valueOf(response.method.uppercase()),
            approveNo = response.approveNo,
            message = response.message,
        )
        log.info("[PAYMENT_CLIENT] 응답 성공 - Result: {}", result)
        return result
    }

    private data class BeeceptorPaymentRequest(
        val paymentKey: String,
        val orderId: String,
        val amount: Long,
    )

    private data class BeeceptorPaymentResponse(
        val externalPaymentKey: String,
        val method: String,
        val approveNo: String,
        val message: String,
    )
}
