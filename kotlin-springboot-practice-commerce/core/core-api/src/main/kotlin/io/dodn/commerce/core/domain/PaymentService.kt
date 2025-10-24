package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import io.dodn.commerce.storage.db.core.PaymentEntity
import io.dodn.commerce.storage.db.core.PaymentRepository
import io.dodn.commerce.storage.db.core.TransactionHistoryEntity
import io.dodn.commerce.storage.db.core.TransactionHistoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val pointHandler: PointHandler,
    private val ownedCouponRepository: OwnedCouponRepository,
) {
    @Transactional
    fun createPayment(
        order: Order,
        paymentDiscount: PaymentDiscount,
    ): Long {
        // 이미 결제완료가 되었는지 확인
        if (paymentRepository.findByOrderId(order.id)?.state == PaymentState.SUCCESS) {
            throw CoreException(ErrorType.ORDER_ALREADY_PAID)
        }

        val payment = PaymentEntity(
            userId = order.userId,
            orderId = order.id,
            originAmount = order.totalPrice,
            ownedCouponId = paymentDiscount.useOwnedCouponId,
            couponDiscount = paymentDiscount.couponDiscount,
            usedPoint = paymentDiscount.usePoint,
            paidAmount = paymentDiscount.paidAmount(order.totalPrice), // 결제 금액 계산
            state = PaymentState.READY,
        )
        return paymentRepository.save(payment).id
    }

    @Transactional
    fun success(orderKey: String, externalPaymentKey: String, amount: BigDecimal): Long {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val payment = paymentRepository.findByOrderId(order.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (payment.userId != order.userId) throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (payment.state != PaymentState.READY) throw CoreException(ErrorType.PAYMENT_INVALID_STATE)
        if (payment.paidAmount != amount) throw CoreException(ErrorType.PAYMENT_AMOUNT_MISMATCH)

        // (중요) 여기까지는 고객한테 아직 돈이 안나간 상태임.
        // PG 사 호출 전에 미리 유효성 검사를 해야 한다.

        /** (핵심)
         * NOTE: PG 승인 API 호출 => 성공 시 다음 로직으로 진행 | 실패 시 예외 발생
         * - 아래는 API 호출 이후의 성공/실패 이후의 로직임. (실제로는 외부 PG사에 API 요청이 이뤄저야한다.)
         * - API 요청에 대해서 PG사로부터 응답값을 받게 된다.
         *  - 응답값 기준으로 외부 결제키, 승인번호 등이 있다.
         */

        payment.success(
            externalPaymentKey,
            // NOTE: PG 승인 API 호출의 응답 값 중 `결제 수단` 넣기
            PaymentMethod.CARD,
            "PG 승인 API 호출의 응답 값 중 `승인번호` 넣기",
        )
        order.paid()

        if (payment.hasAppliedCoupon()) {
            ownedCouponRepository.findByIdOrNull(payment.ownedCouponId)?.use()
        }

        pointHandler.deduct(User(payment.userId), PointType.PAYMENT, payment.id, payment.usedPoint)
        pointHandler.earn(User(payment.userId), PointType.PAYMENT, payment.id, PointAmount.PAYMENT)


        /**
         * Note:
         * - 결제 이력 - 성공/실패 여부를 히스토리를 통해 추적할 수 있게 함.
         * - Order -> T.H
         * - Payment 보단 Order가 더 중요한 개념이라고 봄
         */
        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.PAYMENT,
                userId = order.userId,
                orderId = order.id,
                paymentId = payment.id,
                externalPaymentKey = externalPaymentKey,
                amount = payment.paidAmount,
                message = "결제 성공",
                occurredAt = payment.paidAt!!,
            ),
        )
        return payment.id
    }

    fun fail(orderKey: String, code: String, message: String) {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        val payment = paymentRepository.findByOrderId(order.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.PAYMENT_FAIL,
                userId = order.userId,
                orderId = order.id,
                paymentId = payment.id,
                externalPaymentKey = "",
                amount = BigDecimal.valueOf(-1),
                message = "[$code] $message",
                occurredAt = LocalDateTime.now(),
            ),
        )
    }
}
