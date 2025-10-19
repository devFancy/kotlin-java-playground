package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.CancelAction

data class CancelRequest(
    val orderKey: String,
) {
    fun toCancelAction(): CancelAction {
        return CancelAction(orderKey)
    }
}
