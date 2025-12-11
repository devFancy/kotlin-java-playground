package io.dodn.commerce.core.domain

import io.dodn.commerce.storage.db.core.ProductSectionRepository
import org.springframework.stereotype.Service

@Service
class ProductSectionService(
    private val productSectionRepository: ProductSectionRepository,

) {
    /**
     * 왜 ProductSection 을 나눴을까? -> 충분히 고민하면 좋다.
     * - ProductSection -> 상품 상세 페이지밖에 안쓰임.
     * - 반면에, 상품은 다양한 곳에 쓰임.
     */
    fun findSections(productId: Long): List<ProductSection> {
        return productSectionRepository.findByProductId(productId)
            .filter { it.isActive() }
            .map { ProductSection(it.type, it.content) }
    }
}
