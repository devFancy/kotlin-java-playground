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
    /**
     * Note:
     * (핵심) 리뷰키를 만들어서 범용적으로 리뷰를 쓸 수 있게 해놓은 상태고, 리뷰키를 유니크로 잡아놔서 다양한 조건에 대해 능동적으로 리뷰를 통제할 수 있게 함.
     * - 특정 상품을 최근 14일 이내에 결제 완료를 했고, 아직 리뷰를 작성하지 않은 주문 건에 대해서 새로운 리뷰 작성을 허용한다.
     * - 기간이 지난 주문에 대한 리뷰는 막겠다는 정책이 포함되어 있음.
     * (고민할 부분) 14일에 대한 수정하기 불편할텐데, 이러한 부분을 어떻게 개선할지 고민할 필요가 있음. => 정책에 대한 걸 응집하는 식으로 구현하는게 더 좋다. (Kotlin - Object / Java - Static Factory)
     */
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

    /**
     * Note:
     * - 리뷰가 작성된 지 7일이 지난 이후부터는 수정을 할 수 없는 정책이 포함되어 있음.
     */
    fun validateUpdate(user: User, reviewId: Long) {
        val review = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (review.createdAt.plusDays(7).isBefore(LocalDateTime.now())) throw CoreException(ErrorType.REVIEW_UPDATE_EXPIRED)
    }
}
