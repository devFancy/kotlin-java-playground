package io.dodn.commerce.core.domain

import io.dodn.commerce.ContextTest
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.enums.PointType
import io.dodn.commerce.storage.db.core.OrderEntity
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.PaymentEntity
import io.dodn.commerce.storage.db.core.PaymentRepository
import io.dodn.commerce.storage.db.core.PointBalanceEntity
import io.dodn.commerce.storage.db.core.PointBalanceRepository
import io.dodn.commerce.storage.db.core.PointHistoryRepository
import io.dodn.commerce.storage.db.core.TransactionHistoryRepository
import org.assertj.core.api.Assertions.assertThat
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

        // payment READY
        val payment = paymentRepository.save(
            PaymentEntity(
                userId = userId,
                orderId = order.id,
                originAmount = orderPrice,
                ownedCouponId = 0,
                couponDiscount = couponDiscount,
                usedPoint = usePoint,
                paidAmount = paidAmount,
                state = PaymentState.READY,
            ),
        )

        // when
        val resultPaymentId = paymentService.success(orderKey, "PG-EXT-KEY", paidAmount)

        // then
        // payment updated
        val updatedPayment = paymentRepository.findById(resultPaymentId).orElseThrow()
        assertThat(updatedPayment.state).isEqualTo(PaymentState.SUCCESS)
        assertThat(updatedPayment.externalPaymentKey).isEqualTo("PG-EXT-KEY")

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
}
