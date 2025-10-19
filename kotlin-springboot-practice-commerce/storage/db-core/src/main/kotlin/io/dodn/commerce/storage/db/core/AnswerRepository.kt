package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepository : JpaRepository<AnswerEntity, Long> {
    fun findByQuestionIdIn(questionId: List<Long>): List<AnswerEntity>
}
