package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.enums.SettlementState
import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.storage.db.core.CancelRepository
import io.dodn.commerce.storage.db.core.PaymentRepository
import io.dodn.commerce.storage.db.core.SettlementEntity
import io.dodn.commerce.storage.db.core.SettlementRepository
import io.dodn.commerce.storage.db.core.SettlementTargetRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.collections.iterator

@Service
class SettlementService(
    private val paymentRepository: PaymentRepository,
    private val cancelRepository: CancelRepository,
    private val settlementTargetRepository: SettlementTargetRepository,
    private val settlementRepository: SettlementRepository,
    private val settlementTargetLoader: SettlementTargetLoader,
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun loadTargets(settleDate: LocalDate, from: LocalDateTime, to: LocalDateTime) {
        var paymentPageable: Pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        do {
            val payments = paymentRepository.findAllByStateAndPaidAtBetween(PaymentState.SUCCESS, from, to, paymentPageable)
            try {
                settlementTargetLoader.process(settleDate, TransactionType.PAYMENT, payments.content.associate { it.orderId to it.id })
            } catch (e: Exception) {
                log.error("[SETTLEMENT_LOAD_TARGETS] `결제` 거래건 정산 대상 생성 중 오류 발생 offset: {} size: {} page: {} error: {}", paymentPageable.offset, paymentPageable.pageSize, paymentPageable.pageNumber, e.message, e)
            }
            paymentPageable = payments.nextPageable()
        } while (payments.hasNext())

        var cancelPageable: Pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        do {
            val cancels = cancelRepository.findAllByCanceledAtBetween(from, to, cancelPageable)
            try {
                settlementTargetLoader.process(settleDate, TransactionType.CANCEL, cancels.content.associate { it.orderId to it.id })
            } catch (e: Exception) {
                log.error("[SETTLEMENT_LOAD_TARGETS] `취소` 거래건 정산 대상 생성 중 오류 발생 offset: {} size: {} page: {} error: {}", cancelPageable.offset, cancelPageable.pageSize, cancelPageable.pageNumber, e.message, e)
            }
            cancelPageable = cancels.nextPageable()
        } while (cancels.hasNext())
    }

    @Transactional
    fun calculate(settleDate: LocalDate): Int {
        val summary = settlementTargetRepository.findSummary(settleDate)
        val settlements = summary.map {
            val amount = SettlementCalculator.calculate(it.targetAmount)
            SettlementEntity(
                merchantId = it.merchantId,
                settlementDate = it.settlementDate,
                originalAmount = amount.originalAmount,
                feeAmount = amount.feeAmount,
                feeRate = amount.feeRate,
                settlementAmount = amount.settlementAmount,
                state = SettlementState.READY,
            )
        }
        settlementRepository.saveAll(settlements)
        return settlements.size
    }

    fun transfer(): Int {
        val settlements = settlementRepository.findByState(SettlementState.READY)
            .groupBy { it.merchantId }

        for (settlement in settlements) {
            try {
                val transferAmount = settlement.value.sumOf { it.settlementAmount }
                if (transferAmount <= BigDecimal.ZERO) {
                    // NOTE: 총 정산금이 음수라면 돈 보낼 필요가 없다는 것이므로, 정산금이 양수가 될 때까지 스킵
                    log.warn("[SETTLEMENT_TRANSFER] {} 가맹점 미정산 금액 : {} 발생 확인 요망!", settlement.key, transferAmount)
                    continue
                }

                /**
                 * NOTE: 외부 펌 등 이체 서비스 API 호출
                 */

                settlement.value.forEach { it.sent() }
                settlementRepository.saveAll(settlement.value)
            } catch (e: Exception) {
                log.error("[SETTLEMENT_TRANSFER] {} 가맹점 정산 중 에러 발생: {}", settlement.key, e.message, e)
            }
        }
        return settlements.size
    }
}
