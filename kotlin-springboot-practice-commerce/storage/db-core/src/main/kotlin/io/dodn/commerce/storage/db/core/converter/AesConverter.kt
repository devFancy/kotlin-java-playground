package io.dodn.commerce.storage.db.core.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
internal class AesConverter(
    private val aesHelper: AesHelper,
) : AttributeConverter<String, String> {
    override fun convertToDatabaseColumn(attribute: String?): String? {
        return attribute?.let { aesHelper.encrypt(attribute) }
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        return dbData?.let { aesHelper.decrypt(dbData) }
    }
}
