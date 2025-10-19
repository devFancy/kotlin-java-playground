package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.response.PointResponse
import io.dodn.commerce.core.domain.PointService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PointController(
    private val pointService: PointService,
) {
    @GetMapping("/v1/point")
    fun getPoint(user: User): ApiResponse<PointResponse> {
        val balance = pointService.balance(user)
        val histories = pointService.histories(user)
        return ApiResponse.success(PointResponse.of(balance, histories))
    }
}
