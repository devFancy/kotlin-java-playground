package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.request.CancelRequest
import io.dodn.commerce.core.domain.CancelService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CancelController(
    private val cancelService: CancelService,
) {
    @PostMapping("/v1/cancel")
    fun cancel(
        user: User,
        @RequestBody request: CancelRequest,
    ): ApiResponse<Any> {
        cancelService.cancel(user, request.toCancelAction())
        return ApiResponse.success()
    }
}
