package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.storage.db.CoreDbContextTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OrderItemRepositoryTest(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
) : CoreDbContextTest() {

    @Test
    fun `조건에_맞는_주문상품만_조회_되어야한다`() {
        // given
        val userId = 100L
        val productId = 10L

        // 포함 대상: 해당 유저, PAID, ACTIVE, fromDate 이후, 해당 상품
        val includedOrder = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = "ORDER_KEY_INCLUDED",
                name = "Included Order",
                totalPrice = BigDecimal(1000),
                state = OrderState.PAID,
            ),
        )
        val includedItem = orderItemRepository.save(
            OrderItemEntity(
                orderId = includedOrder.id,
                productId = productId,
                productName = "Prod",
                thumbnailUrl = "http://example.com/thumb.jpg",
                shortDescription = "desc",
                quantity = 1,
                unitPrice = BigDecimal(1000),
                totalPrice = BigDecimal(1000),
            ),
        )

        // 동일 조건이지만 다른 유저
        val otherUserOrder = orderRepository.save(
            OrderEntity(
                userId = 200L,
                orderKey = "ORDER_KEY_OTHER_USER",
                name = "Other User Order",
                totalPrice = BigDecimal(1000),
                state = OrderState.PAID,
            ),
        )
        orderItemRepository.save(
            OrderItemEntity(
                orderId = otherUserOrder.id,
                productId = productId,
                productName = "Prod",
                thumbnailUrl = "http://example.com/thumb.jpg",
                shortDescription = "desc",
                quantity = 1,
                unitPrice = BigDecimal(1000),
                totalPrice = BigDecimal(1000),
            ),
        )

        // 동일 유저지만 CREATED 상태
        val createdOrder = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = "ORDER_KEY_CREATED",
                name = "Created Order",
                totalPrice = BigDecimal(1000),
                state = OrderState.CREATED,
            ),
        )
        orderItemRepository.save(
            OrderItemEntity(
                orderId = createdOrder.id,
                productId = productId,
                productName = "Prod",
                thumbnailUrl = "http://example.com/thumb.jpg",
                shortDescription = "desc",
                quantity = 1,
                unitPrice = BigDecimal(1000),
                totalPrice = BigDecimal(1000),
            ),
        )

        // 동일 유저, PAID지만 주문이 삭제됨
        val deletedOrder = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = "ORDER_KEY_DELETED",
                name = "Deleted Order",
                totalPrice = BigDecimal(1000),
                state = OrderState.PAID,
            ),
        ).also {
            it.delete()
            orderRepository.save(it)
        }
        orderItemRepository.save(
            OrderItemEntity(
                orderId = deletedOrder.id,
                productId = productId,
                productName = "Prod",
                thumbnailUrl = "http://example.com/thumb.jpg",
                shortDescription = "desc",
                quantity = 1,
                unitPrice = BigDecimal(1000),
                totalPrice = BigDecimal(1000),
            ),
        )

        // 동일 유저, PAID지만 아이템이 삭제됨
        val orderForDeletedItem = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = "ORDER_KEY_ITEM_DELETED",
                name = "Order For Deleted Item",
                totalPrice = BigDecimal(1000),
                state = OrderState.PAID,
            ),
        )
        orderItemRepository.save(
            OrderItemEntity(
                orderId = orderForDeletedItem.id,
                productId = productId,
                productName = "Prod",
                thumbnailUrl = "http://example.com/thumb.jpg",
                shortDescription = "desc",
                quantity = 1,
                unitPrice = BigDecimal(1000),
                totalPrice = BigDecimal(1000),
            ),
        ).also {
            it.delete()
            orderItemRepository.save(it)
        }

        // 동일 유저, PAID, ACTIVE지만 다른 상품
        val orderOtherProduct = orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = "ORDER_KEY_OTHER_PRODUCT",
                name = "Order Other Product",
                totalPrice = BigDecimal(1000),
                state = OrderState.PAID,
            ),
        )
        orderItemRepository.save(
            OrderItemEntity(
                orderId = orderOtherProduct.id,
                productId = 999L,
                productName = "Other Prod",
                thumbnailUrl = "http://example.com/thumb2.jpg",
                shortDescription = "desc2",
                quantity = 1,
                unitPrice = BigDecimal(2000),
                totalPrice = BigDecimal(2000),
            ),
        )

        // when
        // fromDate는 포함 대상 주문의 생성시간 직전으로 설정하여 포함되도록 함
        val fromDate = includedOrder.createdAt.minusSeconds(1)
        val result = orderItemRepository.findRecentOrderItemsForProduct(
            userId = userId,
            productId = productId,
            state = OrderState.PAID,
            fromDate = fromDate,
            status = EntityStatus.ACTIVE,
        )

        // then
        assertThat(result.map { it.id }).containsExactly(includedItem.id)
    }
}
