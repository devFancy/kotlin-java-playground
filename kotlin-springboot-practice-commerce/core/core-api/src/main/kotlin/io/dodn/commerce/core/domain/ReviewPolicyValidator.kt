package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.ReviewTargetType
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.ReviewRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ReviewPolicyValidator(
    private val orderItemRepository: OrderItemRepository,
    private val reviewRepository: ReviewRepository,
) {
    fun validateNew(user: User, target: ReviewTarget): ReviewKey {
        if (target.type == ReviewTargetType.PRODUCT) {
            val reviewKeys = orderItemRepository.findRecentOrderItemsForProduct(user.id, target.id, OrderState.PAID, LocalDateTime.now().minusDays(14), EntityStatus.ACTIVE)
                .map { "ORDER_ITEM_${it.id}" }

            val existReviewKeys = reviewRepository.findByUserIdAndReviewKeyIn(user.id, reviewKeys).map { it.reviewKey }.toSet()

            return ReviewKey(
                user = user,
                key = reviewKeys.firstOrNull { it !in existReviewKeys } ?: throw CoreException(ErrorType.REVIEW_HAS_NOT_ORDER),
            )
        }
        throw UnsupportedOperationException()
    }

    fun validateUpdate(user: User, reviewId: Long) {
        val review = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (review.createdAt.plusDays(7).isBefore(LocalDateTime.now())) throw CoreException(ErrorType.REVIEW_UPDATE_EXPIRED)
    }
}
