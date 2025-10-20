package io.dodn.commerce.core.domain

/**
 * Note:
 * - QnA -> 중요 개념
 * - Question, Answer -> 부수 개념
 */
data class QnA(
    val question: Question,
    val answer: Answer,
)
