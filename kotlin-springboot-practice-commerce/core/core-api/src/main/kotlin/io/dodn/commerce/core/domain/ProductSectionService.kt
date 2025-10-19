package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class ProductSectionService(
    private val productFinder: ProductFinder,
) {
    fun findSections(productId: Long): List<ProductSection> {
        return productFinder.findSections(productId)
    }
}
