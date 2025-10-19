package io.dodn.commerce.core.support

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

data class OffsetLimit(
    val offset: Int,
    val limit: Int,
) {
    fun toPageable(): Pageable {
        return PageRequest.of(offset / limit, limit)
    }
}
