package io.dodn.commerce.core.domain

import io.dodn.commerce.ContextTest
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderEntity
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.PaymentEntity
import io.dodn.commerce.storage.db.core.PaymentRepository
import io.dodn.commerce.storage.db.core.PointBalanceEntity
import io.dodn.commerce.storage.db.core.PointBalanceRepository
import io.dodn.commerce.storage.db.core.PointHistoryRepository
import io.dodn.commerce.storage.db.core.TransactionHistoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

class PaymentServiceTest(
    private val paymentService: PaymentService,
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val pointBalanceRepository: PointBalanceRepository,
    private val pointHistoryRepository: PointHistoryRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
) : ContextTest() {

    @Test
    @Transactional
    fun `결제 성공 시 포인트 차감 및 적립과 히스토리가 기록된다`() {
        // given
        val userId = 1234L
        val initialPoint = BigDecimal.valueOf(5000)
        val usePoint = BigDecimal.valueOf(1200)
        val orderPrice = BigDecimal.valueOf(10000)
        val couponDiscount = BigDecimal.ZERO
        val paidAmount = orderPrice - (couponDiscount + usePoint)
        val orderKey = "ORDER-KEY-123"

        // user point balance
        pointBalanceRepository.save(PointBalanceEntity(userId = userId, balance = initialPoint))

        // order in CREATED
        val order = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = orderKey,
                name = "테스트 주문",
                totalPrice = orderPrice,
                state = OrderState.CREATED,
            ),
        )

        // payment status: IN_PROGRESS
        val payment = paymentRepository.save(
            PaymentEntity(
                userId = userId,
                orderId = order.id,
                originAmount = orderPrice,
                ownedCouponId = 0,
                couponDiscount = couponDiscount,
                usedPoint = usePoint,
                paidAmount = paidAmount,
                state = PaymentState.IN_PROGRESS,
                externalPaymentKey = "PG-EXT-KEY",
            ),
        )

        // 외부 결제 결과 객체 생성 (PG사에서 응답받았다고 가정)
        val paymentResult = PaymentResult(
            externalPaymentKey = "PG-EXT-KEY",
            method = PaymentMethod.CARD,
            approveNo = "APPROVE-1234",
            message = "결제 성공",
        )

        // when
        val resultPaymentId = paymentService.completePayment(orderKey, paymentResult)
        // then
        // payment updated
        val updatedPayment = paymentRepository.findById(resultPaymentId).orElseThrow()
        assertThat(updatedPayment.state).isEqualTo(PaymentState.SUCCESS)
        assertThat(updatedPayment.externalPaymentKey).isEqualTo("PG-EXT-KEY")
        assertThat(updatedPayment.method).isEqualTo(PaymentMethod.CARD)
        assertThat(updatedPayment.approveCode).isEqualTo("APPROVE-1234")

        // order updated
        val updatedOrder = orderRepository.findById(order.id).orElseThrow()
        assertThat(updatedOrder.state).isEqualTo(OrderState.PAID)

        // point balance: initial - usePoint + earn(PAYMENT)
        val balance = pointBalanceRepository.findByUserId(userId)!!
        val expectedBalance = (initialPoint - usePoint) + PointAmount.PAYMENT
        assertThat(balance.balance).isEqualByComparingTo(expectedBalance)

        // point history should have 2 entries: -usePoint, +earn
        val histories = pointHistoryRepository.findByUserId(userId).sortedBy { it.id }
        assertThat(histories).hasSize(2)
        assertThat(histories[0].type).isEqualByComparingTo(PointType.PAYMENT)
        assertThat(histories[0].referenceId).isEqualByComparingTo(resultPaymentId)
        assertThat(histories[0].amount).isEqualByComparingTo(usePoint.negate())
        assertThat(histories[0].balanceAfter).isEqualByComparingTo(initialPoint - usePoint)
        assertThat(histories[1].type).isEqualByComparingTo(PointType.PAYMENT)
        assertThat(histories[1].referenceId).isEqualByComparingTo(resultPaymentId)
        assertThat(histories[1].amount).isEqualByComparingTo(PointAmount.PAYMENT)
        assertThat(histories[1].balanceAfter).isEqualByComparingTo(expectedBalance)

        // transaction history saved for payment
        val txs = transactionHistoryRepository.findAll()
        assertThat(txs).anySatisfy {
            assertThat(it.paymentId).isEqualTo(payment.id)
            assertThat(it.userId).isEqualTo(userId)
            assertThat(it.orderId).isEqualTo(order.id)
            assertThat(it.externalPaymentKey).isEqualTo("PG-EXT-KEY")
            assertThat(it.amount).isEqualByComparingTo(paidAmount)
        }
    }

    @Test
    @Transactional
    fun `결제 실패(fail) 호출 시 실패 히스토리가 기록된다`() {
        // given
        val userId = 1234L
        val orderPrice = BigDecimal.valueOf(10000)
        val orderKey = "ORDER-KEY-FAIL"

        // order in CREATED
        val order = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = orderKey,
                name = "실패 테스트 주문",
                totalPrice = orderPrice,
                state = OrderState.CREATED,
            ),
        )

        // payment status: IN_PROGRESS
        val payment = paymentRepository.save(
            PaymentEntity(
                userId = userId,
                orderId = order.id,
                originAmount = orderPrice,
                ownedCouponId = 0,
                couponDiscount = BigDecimal.ZERO,
                usedPoint = BigDecimal.ZERO,
                paidAmount = orderPrice,
                state = PaymentState.IN_PROGRESS,
                externalPaymentKey = "PG-EXT-KEY",
            ),
        )

        // when
        paymentService.fail(orderKey, "PAYMENT_REJECTED", "잔액 부족")

        // then
        val histories = transactionHistoryRepository.findAll()
        assertThat(histories).hasSize(1)
        val history = histories[0]

        assertThat(history.type).isEqualTo(TransactionType.PAYMENT_FAIL)
        assertThat(history.paymentId).isEqualTo(payment.id)
        assertThat(history.message).contains("잔액 부족")
        assertThat(history.amount).isEqualByComparingTo(BigDecimal.valueOf(-1))
    }

    @Test
    @Transactional
    fun `결제 검증(prepare) 시 요청 금액과 실제 결제 금액이 다르면 예외가 발생한다`() {
        // given
        val userId = 1234L
        val orderPrice = BigDecimal.valueOf(10000)
        val wrongAmount = BigDecimal.valueOf(5000) // 금액 변조 시도
        val orderKey = "ORDER-KEY-MISMATCH"

        val order = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = orderKey,
                name = "금액 불일치 테스트",
                totalPrice = orderPrice,
                state = OrderState.CREATED,
            ),
        )

        paymentRepository.save(
            PaymentEntity(
                userId = userId,
                orderId = order.id,
                originAmount = orderPrice,
                ownedCouponId = 0,
                couponDiscount = BigDecimal.ZERO,
                usedPoint = BigDecimal.ZERO,
                paidAmount = orderPrice, // DB에 저장된 금액은 10000
                state = PaymentState.READY,
            ),
        )

        // when & then
        val exception = assertThrows(CoreException::class.java) {
            paymentService.preparePayment(orderKey, "PG-KEY-TEMP", wrongAmount)
        }

        assertThat(exception.errorType).isEqualTo(ErrorType.PAYMENT_AMOUNT_MISMATCH)
    }

    @Test
    @Transactional
    fun `결제 검증(prepare) 시 결제 상태가 READY가 아니면 예외가 발생한다`() {
        // given
        val userId = 1234L
        val orderPrice = BigDecimal.valueOf(10000)
        val orderKey = "ORDER-KEY-INVALID-STATE"

        val order = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = orderKey,
                name = "상태 검증 테스트",
                totalPrice = orderPrice,
                state = OrderState.CREATED,
            ),
        )

        // 이미 IN_PROGRESS 상태 (중복 호출 상황 등)
        paymentRepository.save(
            PaymentEntity(
                userId = userId,
                orderId = order.id,
                originAmount = orderPrice,
                ownedCouponId = 0,
                couponDiscount = BigDecimal.ZERO,
                usedPoint = BigDecimal.ZERO,
                paidAmount = orderPrice,
                state = PaymentState.IN_PROGRESS,
                externalPaymentKey = "PG-KEY-TEMP",
            ),
        )

        // when & then
        val exception = assertThrows(CoreException::class.java) {
            paymentService.preparePayment(orderKey, "PG-KEY-TEMP", orderPrice)
        }

        assertThat(exception.errorType).isEqualTo(ErrorType.PAYMENT_INVALID_STATE)
    }

    @Test
    @Transactional
    fun `결제 완료(complete) 시 결제 상태가 IN_PROGRESS가 아니면 예외가 발생한다`() {
        // given
        val userId = 1234L
        val orderPrice = BigDecimal.valueOf(10000)
        val orderKey = "ORDER-KEY-NOT-PROGRESS"

        val order = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = orderKey,
                name = "완료 상태 검증 테스트",
                totalPrice = orderPrice,
                state = OrderState.CREATED,
            ),
        )

        // prepare 단계를 거치지 않고(READY) 바로 complete 호출 시도
        paymentRepository.save(
            PaymentEntity(
                userId = userId,
                orderId = order.id,
                originAmount = orderPrice,
                ownedCouponId = 0,
                couponDiscount = BigDecimal.ZERO,
                usedPoint = BigDecimal.ZERO,
                paidAmount = orderPrice,
                state = PaymentState.READY,
            ),
        )

        val paymentResult = PaymentResult("PG-KEY", PaymentMethod.CARD, "APPROVE", "성공")

        // when & then
        val exception = assertThrows(CoreException::class.java) {
            paymentService.completePayment(orderKey, paymentResult)
        }

        assertThat(exception.errorType).isEqualTo(ErrorType.PAYMENT_INVALID_STATE)
    }
}
