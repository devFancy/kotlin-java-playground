package io.dodn.commerce.core.support

data class Page<T>(
    val content: List<T>,
    val hasNext: Boolean,
)
