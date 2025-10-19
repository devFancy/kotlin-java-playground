package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.storage.db.CoreDbContextTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class SettlementTargetRepositoryTest(
    private val settlementTargetRepository: SettlementTargetRepository,
) : CoreDbContextTest() {

    @Test
    fun `정산일자별_가맹점_집계가_정상적으로_조회_되어야한다`() {
        // given
        val date = LocalDate.now().minusMonths(1)

        // 가맹점 1: 2개의 행, 주문 ID 중복으로 DISTINCT 주문 수 집계 검증
        settlementTargetRepository.save(
            SettlementTargetEntity(
                merchantId = 1L,
                settlementDate = date,
                targetAmount = BigDecimal(1000),
                transactionType = TransactionType.PAYMENT,
                transactionId = 10L,
                orderId = 100L,
                productId = 1000L,
                quantity = 1,
                unitPrice = BigDecimal(1000),
                totalPrice = BigDecimal(1000),
            ),
        )
        settlementTargetRepository.save(
            SettlementTargetEntity(
                merchantId = 1L,
                settlementDate = date,
                targetAmount = BigDecimal(2000),
                transactionType = TransactionType.PAYMENT,
                transactionId = 11L,
                orderId = 100L, // 동일 주문 ID로 COUNT(DISTINCT orderId) 검증
                productId = 1001L,
                quantity = 2,
                unitPrice = BigDecimal(1000),
                totalPrice = BigDecimal(2000),
            ),
        )

        // 가맹점 2: 동일 정산일에 1개의 행
        settlementTargetRepository.save(
            SettlementTargetEntity(
                merchantId = 2L,
                settlementDate = date,
                targetAmount = BigDecimal(3000),
                transactionType = TransactionType.PAYMENT,
                transactionId = 12L,
                orderId = 200L,
                productId = 2000L,
                quantity = 3,
                unitPrice = BigDecimal(1000),
                totalPrice = BigDecimal(3000),
            ),
        )

        // 다른 정산일 데이터는 제외되어야 함
        settlementTargetRepository.save(
            SettlementTargetEntity(
                merchantId = 1L,
                settlementDate = date.minusDays(1),
                targetAmount = BigDecimal(9999),
                transactionType = TransactionType.PAYMENT,
                transactionId = 13L,
                orderId = 300L,
                productId = 3000L,
                quantity = 9,
                unitPrice = BigDecimal(1111),
                totalPrice = BigDecimal(9999),
            ),
        )

        // when (실행)
        val summaries = settlementTargetRepository.findSummary(date)

        // then (검증)
        // 두 개의 요약 레코드를 기대 (가맹점 1, 2)
        assertThat(summaries).hasSize(2)

        val byMerchant = summaries.associateBy { it.merchantId }

        val m1 = byMerchant[1L]!!
        assertThat(m1.settlementDate).isEqualTo(date)
        assertThat(m1.targetAmount).isEqualByComparingTo(BigDecimal(3000)) // 1000 + 2000 합계
        assertThat(m1.targetCount).isEqualTo(2) // 두 개 행
        assertThat(m1.orderCount).isEqualTo(1) // 주문 ID 100 하나만 DISTINCT

        val m2 = byMerchant[2L]!!
        assertThat(m2.settlementDate).isEqualTo(date)
        assertThat(m2.targetAmount).isEqualByComparingTo(BigDecimal(3000))
        assertThat(m2.targetCount).isEqualTo(1)
        assertThat(m2.orderCount).isEqualTo(1)
    }

    @Test
    fun `정산일자별_가맹점_집계가_취소를_포함하여도_정상적으로_조회_되어야한다`() {
        // given
        val date = LocalDate.now()

        // 가맹점 1: 결제 2건 + 취소 1건(음수)
        settlementTargetRepository.save(
            SettlementTargetEntity(
                merchantId = 1L,
                settlementDate = date,
                targetAmount = BigDecimal(1500),
                transactionType = TransactionType.PAYMENT,
                transactionId = 101L,
                orderId = 1000L,
                productId = 9000L,
                quantity = 1,
                unitPrice = BigDecimal(1500),
                totalPrice = BigDecimal(1500),
            ),
        )
        settlementTargetRepository.save(
            SettlementTargetEntity(
                merchantId = 1L,
                settlementDate = date,
                targetAmount = BigDecimal(500),
                transactionType = TransactionType.PAYMENT,
                transactionId = 102L,
                orderId = 1001L,
                productId = 9001L,
                quantity = 1,
                unitPrice = BigDecimal(500),
                totalPrice = BigDecimal(500),
            ),
        )
        settlementTargetRepository.save(
            SettlementTargetEntity(
                merchantId = 1L,
                settlementDate = date,
                targetAmount = BigDecimal(-500),
                transactionType = TransactionType.CANCEL,
                transactionId = 103L,
                orderId = 1001L, // 동일 주문에 대한 취소
                productId = 9001L,
                quantity = 1,
                unitPrice = BigDecimal(500),
                totalPrice = BigDecimal(500),
            ),
        )

        // 가맹점 2: 결제 1건, 취소 1건(음수) -> 순합 0, 주문 2개(DISTINCT)
        settlementTargetRepository.save(
            SettlementTargetEntity(
                merchantId = 2L,
                settlementDate = date,
                targetAmount = BigDecimal(700),
                transactionType = TransactionType.PAYMENT,
                transactionId = 201L,
                orderId = 2000L,
                productId = 8000L,
                quantity = 1,
                unitPrice = BigDecimal(700),
                totalPrice = BigDecimal(700),
            ),
        )
        settlementTargetRepository.save(
            SettlementTargetEntity(
                merchantId = 2L,
                settlementDate = date,
                targetAmount = BigDecimal(-700),
                transactionType = TransactionType.CANCEL,
                transactionId = 202L,
                orderId = 2001L,
                productId = 8001L,
                quantity = 1,
                unitPrice = BigDecimal(700),
                totalPrice = BigDecimal(700),
            ),
        )

        // when
        val summaries = settlementTargetRepository.findSummary(date)

        // then
        assertThat(summaries).hasSize(2)
        val byMerchant = summaries.associateBy { it.merchantId }

        val m1 = byMerchant[1L]!!
        assertThat(m1.targetAmount).isEqualByComparingTo(BigDecimal(1500)) // 1500 + 500 - 500
        assertThat(m1.targetCount).isEqualTo(3) // 결제 2 + 취소 1
        assertThat(m1.orderCount).isEqualTo(2) // 1000, 1001

        val m2 = byMerchant[2L]!!
        assertThat(m2.targetAmount).isEqualByComparingTo(BigDecimal(0)) // 700 - 700
        assertThat(m2.targetCount).isEqualTo(2)
        assertThat(m2.orderCount).isEqualTo(2) // 2000, 2001
    }
}
