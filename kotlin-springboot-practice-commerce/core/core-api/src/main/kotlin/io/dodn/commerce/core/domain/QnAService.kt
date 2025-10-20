package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.AnswerRepository
import io.dodn.commerce.storage.db.core.QuestionEntity
import io.dodn.commerce.storage.db.core.QuestionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

/**
 * Note:
 * - 이런 느낌의 코드도 흔하게 있음.
 * - CRUD -> 간단하면 심플하게 끝날 수 있음.
 * - (강의자 생각) 소프트웨어가 점점 커진다는 기준에서 서비스 쪽에 트랜잭션을 거는걸 선호하지 않음.
 * - '간단한 부분은 이런 식으로 구현했다' 라는 식으로 이해하면 좋다.
 */
@Service
class QnAService(
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
) {
    fun findQnA(productId: Long, offsetLimit: OffsetLimit): Page<QnA> {
        val questions = questionRepository.findByProductIdAndStatus(
            productId,
            EntityStatus.ACTIVE,
            offsetLimit.toPageable(),
        )

        val answers = answerRepository.findByQuestionIdIn(questions.content.map { it.id })
            .filter { it.isActive() }
            .associateBy { it.questionId }

        return Page(
            questions.content.map {
                QnA(
                    question = Question(
                        id = it.id,
                        userId = it.userId,
                        title = it.title,
                        content = it.content,
                    ),
                    answer = answers[it.id]?.let { answer ->
                        Answer(answer.id, answer.adminId, answer.content)
                    } ?: Answer.EMPTY,
                )
            },
            questions.hasNext(),
        )
    }

    /**
     * Note:
     * - 상품에 강결합된 개념이라고 보면 된다.
     */
    fun addQuestion(user: User, productId: Long, content: QuestionContent): Long {
        val saved = questionRepository.save(
            QuestionEntity(
                userId = user.id,
                productId = productId,
                title = content.title,
                content = content.content,
            ),
        )
        return saved.id
    }

    @Transactional
    fun updateQuestion(user: User, questionId: Long, content: QuestionContent): Long {
        val found = questionRepository.findByIdAndUserId(questionId, user.id)?.takeIf { it.isActive() } ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.updateContent(content.title, content.content)
        return found.id
    }

    @Transactional
    fun removeQuestion(user: User, questionId: Long): Long {
        val found = questionRepository.findByIdAndUserId(questionId, user.id)?.takeIf { it.isActive() } ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.delete()
        return found.id
    }

    /**
     * NOTE: 답변은어드민 쪽 기능임 -> 별도 서버 또는 별도 API 에 있다고 봐주시면 된다.
     * fun addAnswer(user: User, questionId: Long, content: String): Long {...}
     * fun updateAnswer(user: User, answerId: Long, content: String): Long {...}
     * fun removeAnswer(user: User, answerId: Long): Long {...}
     */
}
