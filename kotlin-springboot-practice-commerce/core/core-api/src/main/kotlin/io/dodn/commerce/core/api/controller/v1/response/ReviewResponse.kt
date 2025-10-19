package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.Review
import io.dodn.commerce.core.enums.ReviewTargetType
import java.math.BigDecimal

data class ReviewResponse(
    val id: Long,
    val targetType: ReviewTargetType,
    val targetId: Long,
    val rate: BigDecimal,
    val content: String,
) {
    companion object {
        fun of(review: Review): ReviewResponse {
            return ReviewResponse(
                id = review.id,
                targetType = review.target.type,
                targetId = review.target.id,
                rate = review.content.rate,
                content = review.content.content,
            )
        }

        fun of(reviews: List<Review>): List<ReviewResponse> {
            return reviews.map { of(it) }
        }
    }
}
