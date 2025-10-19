package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionRepository : JpaRepository<QuestionEntity, Long> {
    fun findByIdAndUserId(id: Long, userId: Long): QuestionEntity?
    fun findByProductIdAndStatus(productId: Long, status: EntityStatus, slice: Pageable): Slice<QuestionEntity>
}
