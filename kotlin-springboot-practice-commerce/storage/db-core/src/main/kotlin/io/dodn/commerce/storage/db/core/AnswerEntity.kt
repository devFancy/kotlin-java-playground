package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "answer")
class AnswerEntity(
    val adminId: Long,
    val questionId: Long,
    val content: String,
) : BaseEntity()
