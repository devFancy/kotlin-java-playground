package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.ReviewTargetType

data class ReviewTarget(
    val type: ReviewTargetType,
    val id: Long,
)
