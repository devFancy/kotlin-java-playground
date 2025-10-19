package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.ReviewContent
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import java.math.BigDecimal

data class UpdateReviewRequest(
    val rate: BigDecimal,
    val content: String,
) {
    fun toContent(): ReviewContent {
        if (rate <= BigDecimal.ZERO) throw CoreException(ErrorType.INVALID_REQUEST)
        if (rate > BigDecimal.valueOf(5.0)) throw CoreException(ErrorType.INVALID_REQUEST)
        if (content.isEmpty()) throw CoreException(ErrorType.INVALID_REQUEST)
        return ReviewContent(rate, content)
    }
}
