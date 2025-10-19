package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.PointType
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "point_history")
class PointHistoryEntity(
    val userId: Long,
    val type: PointType,
    val referenceId: Long,
    val amount: BigDecimal,
    val balanceAfter: BigDecimal,
) : BaseEntity()
