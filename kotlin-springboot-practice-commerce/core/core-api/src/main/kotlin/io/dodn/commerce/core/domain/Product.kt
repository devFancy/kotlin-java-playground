package io.dodn.commerce.core.domain

data class Product(
    val id: Long,
    val name: String,
    val thumbnailUrl: String,
    val description: String,
    val shortDescription: String,
    val price: Price,
)
