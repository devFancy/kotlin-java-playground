package io.dodn.commerce.core.client

import io.dodn.commerce.core.domain.PaymentCommand
import io.dodn.commerce.core.domain.PaymentResult
import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity

@Component
class BeeceptorPaymentClient(
    val restTemplate: RestTemplate,
) : PaymentClient {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun requestPayment(command: PaymentCommand): PaymentResult {
        val url = "https://commerce.free.beeceptor.com/api/v1/payment"
        val header = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }

        val requestPayload = BeeceptorPaymentRequest(
            paymentKey = command.externalPaymentKey,
            orderId = command.orderId.toString(),
            amount = command.amount,
        )
        val requestEntity = HttpEntity(requestPayload, header)
        log.info("[PAYMENT_CLIENT] beeceptor 외부 결제 API 호출. URL: {}, Request: {}", url, requestPayload)

        return try {
            val responseEntity = restTemplate.postForEntity<BeeceptorPaymentResponse>(
                url,
                requestEntity,
            )
            val response = responseEntity.body
                ?: throw CoreException(ErrorType.PAYMENT_EXTERNAL_API_BODY_EMPTY)

            val result = PaymentResult(
                externalPaymentKey = response.externalPaymentKey,
                method = PaymentMethod.valueOf(response.method.uppercase()),
                approveNo = response.approveNo,
                message = response.message,
            )
            log.info("[PAYMENT_CLIENT] 응답 성공 - Result: {}", result)
            result
        } catch (e: Exception) {
            log.error("[PAYMENT_CLIENT] beeceptor 외부 결제 API 호출 실패", e)
            throw CoreException(ErrorType.PAYMENT_EXTERNAL_API_FAIL)
        }
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
