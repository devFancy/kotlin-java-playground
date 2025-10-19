package io.dodn.commerce.core.domain

data class Question(
    val id: Long,
    val userId: Long,
    val title: String,
    val content: String,
)
