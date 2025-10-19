package io.dodn.commerce.core.domain

data class Review(
    val id: Long,
    val userId: Long,
    val target: ReviewTarget,
    val content: ReviewContent,
)
