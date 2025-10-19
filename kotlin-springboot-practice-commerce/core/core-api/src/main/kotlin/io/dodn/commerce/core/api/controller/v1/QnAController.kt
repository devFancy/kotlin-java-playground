package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.request.AddQuestionRequest
import io.dodn.commerce.core.api.controller.v1.request.UpdateQuestionRequest
import io.dodn.commerce.core.api.controller.v1.response.QnAResponse
import io.dodn.commerce.core.domain.QnAService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.response.ApiResponse
import io.dodn.commerce.core.support.response.PageResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class QnAController(
    private val qnaService: QnAService,
) {
    @GetMapping("/v1/qna")
    fun getQnA(
        @RequestParam productId: Long,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
    ): ApiResponse<PageResponse<QnAResponse>> {
        val page = qnaService.findQnA(productId, OffsetLimit(offset, limit))
        return ApiResponse.success(PageResponse(QnAResponse.of(page.content), page.hasNext))
    }

    @PostMapping("/v1/questions")
    fun createQuestion(
        user: User,
        @RequestBody request: AddQuestionRequest,
    ): ApiResponse<Any> {
        qnaService.addQuestion(user, request.productId, request.toContent())
        return ApiResponse.success()
    }

    @PutMapping("/v1/questions/{questionId}")
    fun updateQuestion(
        user: User,
        @PathVariable questionId: Long,
        @RequestBody request: UpdateQuestionRequest,
    ): ApiResponse<Any> {
        qnaService.updateQuestion(user, questionId, request.toContent())
        return ApiResponse.success()
    }

    @DeleteMapping("/v1/questions/{questionId}")
    fun deleteQuestion(
        user: User,
        @PathVariable questionId: Long,
    ): ApiResponse<Any> {
        qnaService.removeQuestion(user, questionId)
        return ApiResponse.success()
    }
}
