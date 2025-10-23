package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CartItemEntity
import io.dodn.commerce.storage.db.core.CartItemRepository
import io.dodn.commerce.storage.db.core.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository,
) {
    /**
     * Note:
     * - Cart: 논리적인 개념 - Cart의 Items을 모은 것.
     * - 실제 상품 가격으로 주문이 되기 때문에, CartItem 쪽에 상품 가격을 두진 않음.
     * -> 상품에 있는 가격을 가져와서 사용한다.
     */
    fun getCart(user: User): Cart {
        val items = cartItemRepository.findByUserIdAndStatus(user.id, EntityStatus.ACTIVE)
        val productMap = productRepository.findAllById(items.map { it.productId })
            .associateBy { it.id }

        return Cart(
            userId = user.id,
            items = items.filter { productMap.containsKey(it.productId) }
                .map {
                    CartItem(
                        id = it.id,
                        product = Product(
                            id = productMap[it.productId]!!.id,
                            name = productMap[it.productId]!!.name,
                            thumbnailUrl = productMap[it.productId]!!.thumbnailUrl,
                            description = productMap[it.productId]!!.description,
                            shortDescription = productMap[it.productId]!!.shortDescription,
                            price = Price(
                                costPrice = productMap[it.productId]!!.costPrice,
                                salesPrice = productMap[it.productId]!!.salesPrice,
                                discountedPrice = productMap[it.productId]!!.discountedPrice,
                            ),
                        ),
                        quantity = it.quantity,
                    )
                },
        )
    }

    @Transactional
    fun addCartItem(user: User, item: AddCartItem): Long {
        return cartItemRepository.findByUserIdAndProductId(user.id, item.productId)?.apply {
            if (isDeleted()) active()
            applyQuantity(item.quantity)
        }?.id ?: cartItemRepository.save(
            CartItemEntity(
                userId = user.id,
                productId = item.productId,
                quantity = item.quantity,
            ),
        ).id
    }

    @Transactional
    fun modifyCartItem(user: User, item: ModifyCartItem): Long {
        val found = cartItemRepository.findByUserIdAndIdAndStatus(user.id, item.cartItemId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.applyQuantity(item.quantity)
        return found.id
    }

    /**
     * Note:
     * - 삭제할 때 실제로 삭제를 하지 않고, '삭제' 상태로 변경하여 관리한다.
     */
    @Transactional
    fun deleteCartItem(user: User, cartItemId: Long) {
        val entity = cartItemRepository.findByUserIdAndIdAndStatus(user.id, cartItemId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        entity.delete()
    }
}
