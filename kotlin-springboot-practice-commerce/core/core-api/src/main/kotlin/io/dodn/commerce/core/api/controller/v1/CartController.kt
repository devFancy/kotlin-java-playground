package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.request.AddCartItemRequest
import io.dodn.commerce.core.api.controller.v1.request.ModifyCartItemRequest
import io.dodn.commerce.core.api.controller.v1.response.CartItemResponse
import io.dodn.commerce.core.api.controller.v1.response.CartResponse
import io.dodn.commerce.core.domain.CartService
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CartController(
    private val cartService: CartService,
) {
    @GetMapping("/v1/cart")
    fun getCart(user: User): ApiResponse<CartResponse> {
        val cart = cartService.getCart(user)
        return ApiResponse.success(CartResponse(cart.items.map { CartItemResponse.of(it) }))
    }

    @PostMapping("/v1/cart/items")
    fun addCartItem(user: User, @RequestBody request: AddCartItemRequest): ApiResponse<Any> {
        cartService.addCartItem(user, request.toAddCartItem())
        return ApiResponse.success()
    }

    @PutMapping("/v1/cart/items/{cartItemId}")
    fun modifyCartItem(
        user: User,
        @PathVariable cartItemId: Long,
        @RequestBody request: ModifyCartItemRequest,
    ): ApiResponse<Any> {
        cartService.modifyCartItem(user, request.toModifyCartItem(cartItemId))
        return ApiResponse.success()
    }

    @DeleteMapping("/v1/cart/items/{cartItemId}")
    fun deleteCartItem(user: User, @PathVariable cartItemId: Long): ApiResponse<Any> {
        cartService.deleteCartItem(user, cartItemId)
        return ApiResponse.success()
    }
}
