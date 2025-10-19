package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.Coupon
import io.dodn.commerce.core.domain.Product
import io.dodn.commerce.core.domain.ProductSection
import io.dodn.commerce.core.domain.RateSummary
import java.math.BigDecimal

data class ProductDetailResponse(
    val name: String,
    val thumbnailUrl: String,
    val description: String,
    val shortDescription: String,
    val costPrice: BigDecimal,
    val salesPrice: BigDecimal,
    val discountedPrice: BigDecimal,
    val rate: BigDecimal,
    val rateCount: Long,
    val sections: List<ProductSectionResponse>,
    val coupons: List<CouponResponse>,
) {
    constructor(
        product: Product,
        sections: List<ProductSection>,
        rateSummary: RateSummary,
        coupons: List<Coupon>,
    ) : this(
        name = product.name,
        thumbnailUrl = product.thumbnailUrl,
        description = product.description,
        shortDescription = product.shortDescription,
        costPrice = product.price.costPrice,
        salesPrice = product.price.salesPrice,
        discountedPrice = product.price.discountedPrice,
        rate = rateSummary.rate,
        rateCount = rateSummary.count,
        sections = sections.map { ProductSectionResponse(it.type, it.content) },
        coupons = coupons.map { CouponResponse.of(it) },
    )
}
