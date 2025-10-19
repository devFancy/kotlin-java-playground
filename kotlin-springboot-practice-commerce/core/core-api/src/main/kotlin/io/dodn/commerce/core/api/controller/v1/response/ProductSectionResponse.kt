package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.enums.ProductSectionType

data class ProductSectionResponse(
    val type: ProductSectionType,
    val content: String,
)
