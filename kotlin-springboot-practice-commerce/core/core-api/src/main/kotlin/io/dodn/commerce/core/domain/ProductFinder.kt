package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.ProductCategoryRepository
import io.dodn.commerce.storage.db.core.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ProductFinder(
    private val productRepository: ProductRepository,
    private val productCategoryRepository: ProductCategoryRepository,
) {
    /**
     * Note:
     * 카테고리 목록을 가져온다. -> categories
     * - 페이징을 맵핑 기준으로 가져온다.
     * - (전제) 상품 카테고리 하나라도 있는 애는 상품을 삭제못하게 한다.
     * - 일반적인 조회나 페이징 처리를 하기 위해서는 어느 정도 규칙을 기반으로 커버한다.
     * (참고) 여기서는 정렬을 고려하지 않았음
     */
    fun findByCategory(categoryId: Long, offsetLimit: OffsetLimit): Page<Product> {
        val categories = productCategoryRepository.findByCategoryIdAndStatus(
            categoryId,
            EntityStatus.ACTIVE,
            offsetLimit.toPageable(),
        )
        val products = productRepository.findAllById(categories.content.map { it.productId })
            .map {
                Product(
                    id = it.id,
                    name = it.name,
                    thumbnailUrl = it.thumbnailUrl,
                    description = it.description,
                    shortDescription = it.shortDescription,
                    price = Price(
                        costPrice = it.costPrice,
                        salesPrice = it.salesPrice,
                        discountedPrice = it.discountedPrice,
                    ),
                )
            }
        return Page(products, categories.hasNext())
    }

    /**
     * Note:
     * 단건 조회 -> findByIdOrNull (삭제된게 아니면 Active)
     * 이후 상품을 생성해서 리턴한다.
     * - Price -> 중요 개념으로 별도 클래스로 생성함
     * - 왜 Price 를 묶었을까? -> 금액 계산 같은 것들이 들어갈 수 있으므로 나중에 효용성이 있기 때문에 응집하도록 클래스를 생성했음.
     */
    fun find(productId: Long): Product {
        val found = productRepository.findByIdOrNull(productId)?.takeIf { it.isActive() }
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        return Product(
            id = found.id,
            name = found.name,
            thumbnailUrl = found.thumbnailUrl,
            description = found.description,
            shortDescription = found.shortDescription,
            price = Price(
                costPrice = found.costPrice,
                salesPrice = found.salesPrice,
                discountedPrice = found.discountedPrice,
            ),
        )
    }
}
