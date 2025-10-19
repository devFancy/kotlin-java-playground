package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.request.ApplyFavoriteRequest
import io.dodn.commerce.core.api.controller.v1.request.ApplyFavoriteRequestType
import io.dodn.commerce.core.api.controller.v1.response.FavoriteResponse
import io.dodn.commerce.core.domain.FavoriteService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.response.ApiResponse
import io.dodn.commerce.core.support.response.PageResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FavoriteController(
    private val favoriteService: FavoriteService,
) {
    @GetMapping("/v1/favorites")
    fun getFavorites(
        user: User,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
    ): ApiResponse<PageResponse<FavoriteResponse>> {
        val page = favoriteService.findFavorites(user, OffsetLimit(offset, limit))
        return ApiResponse.success(PageResponse(FavoriteResponse.of(page.content), page.hasNext))
    }

    @PostMapping("/v1/favorites")
    fun applyFavorite(
        user: User,
        @RequestBody request: ApplyFavoriteRequest,
    ): ApiResponse<Any> {
        when (request.type) {
            ApplyFavoriteRequestType.FAVORITE -> favoriteService.addFavorite(user, request.productId)
            ApplyFavoriteRequestType.UNFAVORITE -> favoriteService.removeFavorite(user, request.productId)
        }
        return ApiResponse.success()
    }
}
