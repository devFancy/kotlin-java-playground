package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.ReviewEntity
import io.dodn.commerce.storage.db.core.ReviewRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
class ReviewManager(
    private val reviewRepository: ReviewRepository,
) {
    /**
     * Note:
     * (실제 서비스 환경) 리뷰에 대한 언쟁, 소송이 많기 때문에 리뷰에 대한 `히스토리` 테이블이 필요함.
     * - 그래서 리뷰에 대한 전체 변환 이력을 히스토리 테이블(or 스냅샷)로 만들어서 쌓아둬야한다.
     * 단, 현재 요구사항에서는 히스토리 관련 내용을 포함해두고 있지 않음.
     */
    fun add(reviewKey: ReviewKey, target: ReviewTarget, content: ReviewContent): Long {
        val saved = reviewRepository.save(
            ReviewEntity(
                userId = reviewKey.user.id,
                reviewKey = reviewKey.key,
                targetType = target.type,
                targetId = target.id,
                rate = content.rate,
                content = content.content,
            ),
        )
        return saved.id
    }

    @Transactional
    fun update(user: User, reviewId: Long, content: ReviewContent): Long {
        val found = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.updateContent(content.rate, content.content)
        return found.id
    }

    @Transactional
    fun delete(user: User, reviewId: Long): Long {
        val found = reviewRepository.findByIdAndUserId(reviewId, user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.delete()
        return found.id
    }
}
