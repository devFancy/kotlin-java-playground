package io.dodn.commerce.core.domain

data class Answer(
    val id: Long,
    val adminId: Long,
    val content: String,
) {
    companion object {
        val EMPTY: Answer = Answer(-1, -1, "")
    }
}
