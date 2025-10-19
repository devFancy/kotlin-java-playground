package io.dodn.commerce.core.api.controller.v1.request

data class ApplyFavoriteRequest(
    val productId: Long,
    val type: ApplyFavoriteRequestType,
)

enum class ApplyFavoriteRequestType {
    FAVORITE,
    UNFAVORITE,
}
