package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.FavoriteEntity
import io.dodn.commerce.storage.db.core.FavoriteRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
) {
    /**
     * Note:
     * - 요구사항: 30일 이내에 찜 목록만 조회한다.
     */
    fun findFavorites(user: User, offsetLimit: OffsetLimit): Page<Favorite> {
        val cutoff = LocalDateTime.now().minusDays(30)
        val result = favoriteRepository.findByUserIdAndStatusAndUpdatedAtAfter(
            user.id,
            EntityStatus.ACTIVE,
            cutoff,
            offsetLimit.toPageable(),
        )
        return Page(
            result.content.map {
                Favorite(
                    id = it.id,
                    userId = it.userId,
                    productId = it.productId,
                    favoritedAt = it.favoritedAt,
                )
            },
            result.hasNext(),
        )
    }

    @Transactional
    fun addFavorite(user: User, productId: Long): Long {
        val existing = favoriteRepository.findByUserIdAndProductId(user.id, productId)
        return if (existing == null) {
            val saved = favoriteRepository.save(
                FavoriteEntity(
                    userId = user.id,
                    productId = productId,
                    favoritedAt = LocalDateTime.now(),
                ),
            )
            saved.id
        } else {
            existing.favorite()
            existing.id
        }
    }

    /**
     * Note:
     * - Tip: 실제로 데이터를 삭제하지 않고, 삭제하는 상태로 바꾼다. -> 이후 데이터 분석 or 사용자 추적을 위해서.
     */
    @Transactional
    fun removeFavorite(user: User, productId: Long): Long {
        val existing = favoriteRepository.findByUserIdAndProductId(user.id, productId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        existing.delete()
        return existing.id
    }
}
