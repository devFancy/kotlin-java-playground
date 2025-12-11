package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
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

    // NOTE: 검증 및 진행 중 처리 (READY -> IN_PROGRESS)
    @Transactional
    fun preparePayment(orderKey: String, externalPaymentKey: String, amount: BigDecimal): PaymentCommand {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val payment = paymentRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        if (payment.userId != order.userId) throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (payment.state != PaymentState.READY) throw CoreException(ErrorType.PAYMENT_INVALID_STATE)
        if (payment.paidAmount.compareTo(amount) != 0) throw CoreException(ErrorType.PAYMENT_AMOUNT_MISMATCH)

        payment.inProgress(externalPaymentKey)

        return PaymentCommand(
            externalPaymentKey = externalPaymentKey,
            orderId = order.id,
            amount = payment.paidAmount.toLong(),
        )
    }

    // NOTE: 결제 완료 처리 (IN_PROGRESS -> SUCCESS)
    // - 만약 여기서 장애가 발생하면? PG사는 결제 완료되었으나, DB는 'IN_PROGRESS' 상태로 남아 데이터 불일치가 발생한다.
    // - 이를 해결하기 위해 배치가 주기적으로 'IN_PROGRESS' 상태이면서 오래된 건들을 조회하여 수행한다.
    @Transactional
    fun completePayment(orderKey: String, paymentResult: PaymentResult): Long {
        val order = orderRepository.findByOrderKeyAndStateAndStatus(orderKey, OrderState.CREATED, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        val payment = paymentRepository.findByOrderId(order.id)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        if (payment.state != PaymentState.IN_PROGRESS) {
            throw CoreException(ErrorType.PAYMENT_INVALID_STATE)
        }
        // 상태 업데이트
        payment.success(
            paymentResult.externalPaymentKey,
            paymentResult.method,
            paymentResult.approveNo,
        )
        order.paid()

        // 쿠폰 및 포인트 처리
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
                externalPaymentKey = paymentResult.externalPaymentKey,
                amount = payment.paidAmount,
                message = paymentResult.message,
                occurredAt = payment.paidAt!!,
            ),
        )
        return payment.id
    }

    /**
     * 결제 실패 처리
     * - Payment 상태는 Fail로 변경 (재시도 가능성 열어둠)
     * - 실패 이력만 History에 기록
     */
    @Transactional
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
