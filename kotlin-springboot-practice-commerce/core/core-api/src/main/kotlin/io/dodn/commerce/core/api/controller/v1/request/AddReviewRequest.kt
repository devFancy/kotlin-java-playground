package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.ReviewContent
import io.dodn.commerce.core.domain.ReviewTarget
import io.dodn.commerce.core.enums.ReviewTargetType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import java.math.BigDecimal

data class AddReviewRequest(
    val userId: Long,
    val targetType: ReviewTargetType,
    val targetId: Long,
    val rate: BigDecimal,
    val content: String,
) {
    fun toTarget(): ReviewTarget {
        return ReviewTarget(targetType, targetId)
    }

    fun toContent(): ReviewContent {
        if (rate <= BigDecimal.ZERO) throw CoreException(ErrorType.INVALID_REQUEST)
        if (rate > BigDecimal.valueOf(5.0)) throw CoreException(ErrorType.INVALID_REQUEST)
        if (content.isEmpty()) throw CoreException(ErrorType.INVALID_REQUEST)
        return ReviewContent(rate, content)
    }
}
