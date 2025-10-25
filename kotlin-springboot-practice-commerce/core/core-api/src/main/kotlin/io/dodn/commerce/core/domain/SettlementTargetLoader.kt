package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.storage.db.core.MerchantProductMappingRepository
import io.dodn.commerce.storage.db.core.OrderItemEntity
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.SettlementTargetEntity
import io.dodn.commerce.storage.db.core.SettlementTargetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SettlementTargetLoader(
    private val settlementTargetRepository: SettlementTargetRepository,
    private val orderItemRepository: OrderItemRepository,
    private val merchantProductMappingRepository: MerchantProductMappingRepository,
) {
    @Transactional
    fun process(
        settleDate: LocalDate,
        transactionType: TransactionType,
        transactionIdMap: Map<Long, Long>,
    ) {
        val orderItems: List<OrderItemEntity> = orderItemRepository.findByOrderIdIn(transactionIdMap.keys)

        /**
         * Note:
         * - merchant: 가맹점
         * - merchantMappingMap: 상품별로 어떤 가맹점이 우리에게 넘겨준 상품인지 매핑한다.
         * - SettlementTarget: 정산 기반 데이터이자 중간 적재 테이블 역할 (흐름: 결제/취소 -> "정산 중간 적재" -> 정산)
         */
        val merchantMappingMap = merchantProductMappingRepository
            .findByProductIdIn(orderItems.map { it.productId }.toSet())
            .associateBy { it.productId }

        val targets = orderItems.map { item ->
            SettlementTargetEntity(
                settlementDate = settleDate,
                merchantId = merchantMappingMap[item.productId]?.merchantId ?: throw IllegalStateException("상품 ${item.productId} 의 가맹점 매핑이 존재하지 않음"),
                transactionType = transactionType,
                transactionId = transactionIdMap[item.orderId] ?: throw IllegalStateException("주문 ${item.orderId} 의 거래 ID 매핑이 존재하지 않음"),
                orderId = item.orderId,
                productId = item.productId,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
                totalPrice = item.totalPrice,
                targetAmount = when (transactionType) {
                    TransactionType.PAYMENT -> item.totalPrice // 양수
                    TransactionType.CANCEL -> item.totalPrice.negate() // 음수
                    else -> throw UnsupportedOperationException()
                },
            )
        }
        settlementTargetRepository.saveAll(targets)
    }
}
