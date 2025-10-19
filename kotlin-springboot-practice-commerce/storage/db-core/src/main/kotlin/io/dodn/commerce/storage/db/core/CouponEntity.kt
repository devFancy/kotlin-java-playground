package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CouponType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "coupon")
class CouponEntity(
    val name: String,
    @Enumerated(EnumType.STRING)
    val type: CouponType,
    val discount: BigDecimal,
    val expiredAt: LocalDateTime,
) : BaseEntity()
