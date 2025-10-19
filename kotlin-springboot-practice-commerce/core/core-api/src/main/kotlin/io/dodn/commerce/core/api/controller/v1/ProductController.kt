package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.api.controller.v1.response.ProductDetailResponse
import io.dodn.commerce.core.api.controller.v1.response.ProductResponse
import io.dodn.commerce.core.domain.CouponService
import io.dodn.commerce.core.domain.ProductSectionService
import io.dodn.commerce.core.domain.ProductService
import io.dodn.commerce.core.domain.ReviewService
import io.dodn.commerce.core.domain.ReviewTarget
import io.dodn.commerce.core.enums.ReviewTargetType
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.response.ApiResponse
import io.dodn.commerce.core.support.response.PageResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productService: ProductService,
    private val productSectionService: ProductSectionService,
    private val reviewService: ReviewService,
    private val couponService: CouponService,
) {
    @GetMapping("/v1/products")
    fun findProducts(
        @RequestParam categoryId: Long,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
    ): ApiResponse<PageResponse<ProductResponse>> {
        val result = productService.findProducts(categoryId, OffsetLimit(offset, limit))
        return ApiResponse.success(PageResponse(ProductResponse.of(result.content), result.hasNext))
    }

    /**
     * Note: findProduct, findSections -> '왜 나눴을까?' 에 대한 고민을 해보자.
     * 강의를 활용하는 팁
     * - 코드 수정을 하면서 스스로 더 나은 방법이 없는지 생각해볼 것. -> Product <-> ProductSection 간의 관계(엔티티 기준)
     * - Product: 상품 목록, 최근 본 상품 등등 -> 다양한 기능에 활용될 수 있음
     * - ProductSection : 상품 상세에 국한되어있음
     */
    @GetMapping("/v1/products/{productId}")
    fun findProduct(
        @PathVariable productId: Long,
    ): ApiResponse<ProductDetailResponse> {
        val product = productService.findProduct(productId)
        val sections = productSectionService.findSections(productId)
        val rateSummary = reviewService.findRateSummary(ReviewTarget(ReviewTargetType.PRODUCT, productId))
        // NOTE: 별도 API 가 나을까? -> 이런 고민이 왜 생기는 요구사항이 UI가 어떤 것들이 있는지 생각해볼 것!
        val coupons = couponService.getCouponsForProducts(listOf(productId))
        return ApiResponse.success(ProductDetailResponse(product, sections, rateSummary, coupons))
    }
}
