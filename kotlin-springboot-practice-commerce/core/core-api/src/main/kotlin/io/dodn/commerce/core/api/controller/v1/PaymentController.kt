package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.request.CreatePaymentRequest
import io.dodn.commerce.core.api.controller.v1.response.CreatePaymentResponse
import io.dodn.commerce.core.domain.OrderService
import io.dodn.commerce.core.domain.OwnedCouponService
import io.dodn.commerce.core.domain.PaymentService
import io.dodn.commerce.core.domain.PointService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.facade.PaymentConfirmFacade
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class PaymentController(
    private val paymentConfirmFacade: PaymentConfirmFacade,
    private val paymentService: PaymentService,
    private val orderService: OrderService,
    private val ownedCouponService: OwnedCouponService,
    private val pointService: PointService,
) {
    /**
     * Note
     * - 결제를 하기 위한 개념적인 틀을 만들고 DB에 저장해놓은 역할.
     */
    @PostMapping("/v1/payments")
    fun create(
        user: User,
        @RequestBody request: CreatePaymentRequest,
    ): ApiResponse<CreatePaymentResponse> {
        val order = orderService.getOrder(user, request.orderKey, OrderState.CREATED)
        val ownedCoupons = ownedCouponService.getOwnedCouponsForCheckout(user, order.items.map { it.productId })
        val pointBalance = pointService.balance(user)
        val id = paymentService.createPayment(
            order = order,
            paymentDiscount = request.toPaymentDiscount(ownedCoupons, pointBalance),
        )
        return ApiResponse.success(CreatePaymentResponse(id))
    }

    /**
     * Note:
     * (토스페이먼츠 PG 사 스펙으로 되어있음)
     * - 아래 2개의 API는 PG사와의 연동 부분.
     * - PG사로부터 성공/실패에 대한 콜백을 우리 서버로 보내준다.
     * - 아래 파라미터 3개는 PG 사의 파라미터 규칙임 -> 규격 문서가 있음.
     */
    @PostMapping("/v1/payments/callback/success")
    fun callbackForSuccess(
        @RequestParam orderId: String,
        @RequestParam paymentKey: String,
        @RequestParam amount: BigDecimal,
    ): ApiResponse<Any> {
        paymentConfirmFacade.success(
            orderKey = orderId,
            externalPaymentKey = paymentKey,
            amount = amount,
        )
        return ApiResponse.success()
    }

    @PostMapping("/v1/payments/callback/fail")
    fun callbackForFail(
        @RequestParam orderId: String,
        @RequestParam code: String,
        @RequestParam message: String,
    ): ApiResponse<Any> {
        paymentConfirmFacade.fail(
            orderKey = orderId,
            code = code,
            message = message,
        )
        return ApiResponse.success()
    }
}
