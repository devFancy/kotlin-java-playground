package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.QuestionContent
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType

data class AddQuestionRequest(
    val productId: Long,
    val title: String,
    val content: String,
) {
    fun toContent(): QuestionContent {
        if (title.isEmpty()) throw CoreException(ErrorType.INVALID_REQUEST)
        if (title.length > 100) throw CoreException(ErrorType.INVALID_REQUEST)
        if (content.isEmpty()) throw CoreException(ErrorType.INVALID_REQUEST)

        return QuestionContent(
            title = title,
            content = content,
        )
    }
}
