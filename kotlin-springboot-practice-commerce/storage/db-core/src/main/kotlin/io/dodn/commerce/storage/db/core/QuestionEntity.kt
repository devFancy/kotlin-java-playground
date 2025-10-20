package io.dodn.commerce.storage.db.core

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "question")
class QuestionEntity(
    val userId: Long,
    /**
     * NOTE: QNA 는 아예 상품 전용으로 지정 -> 개념적으로 Q, A는 상품에 의존하도록 만듬.
     */
    val productId: Long,
    title: String,
    content: String,
) : BaseEntity() {
    var title: String = title
        protected set

    @Column(columnDefinition = "TEXT")
    var content: String = content
        protected set

    fun updateContent(title: String, content: String) {
        this.title = title
        this.content = content
    }
}
