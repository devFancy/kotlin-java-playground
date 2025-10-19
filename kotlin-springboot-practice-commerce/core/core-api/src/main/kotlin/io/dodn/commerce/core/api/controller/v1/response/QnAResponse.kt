package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.QnA

data class QnAResponse(
    val questionId: Long,
    val questionTitle: String,
    val question: String,
    val answerId: Long,
    val answer: String,
) {
    companion object {
        fun of(qna: QnA): QnAResponse {
            return QnAResponse(
                questionId = qna.question.id,
                questionTitle = qna.question.title,
                question = qna.question.content,
                answerId = qna.answer.id,
                answer = qna.answer.content,
            )
        }

        fun of(qna: List<QnA>): List<QnAResponse> {
            return qna.map { of(it) }
        }
    }
}
