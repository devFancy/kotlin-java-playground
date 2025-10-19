package io.dodn.commerce.storage.db.core.converter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "dodn.storage.core.security")
internal data class SecurityProperty(
    val key: String,
    val iv: String,
)
