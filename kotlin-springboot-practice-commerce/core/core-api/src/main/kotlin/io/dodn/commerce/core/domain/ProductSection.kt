package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.ProductSectionType

data class ProductSection(
    val type: ProductSectionType,
    val content: String,
)
