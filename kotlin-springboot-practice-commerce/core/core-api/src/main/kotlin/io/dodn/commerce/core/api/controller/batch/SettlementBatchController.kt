package io.dodn.commerce.core.api.controller.batch

import io.dodn.commerce.core.domain.SettlementService
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class SettlementBatchController(
    private val settlementService: SettlementService,
) {
    /**
     * NOTE: 정산 대상 적재 배치
     * - 오전 1시 실행
     * - 어제 00:00:00 ~ 23:59:59 데이터 기준으로 정산 데이터 적재
     */
    @PostMapping("/internal-batch/load-targets")
    fun loadTargets(@RequestParam targetDate: LocalDate = LocalDate.now()): ApiResponse<Any> {
        settlementService.loadTargets(
            settleDate = targetDate,
            from = targetDate.minusDays(1).atStartOfDay(),
            to = targetDate.atStartOfDay().minusNanos(1),
        )
        return ApiResponse.success()
    }

    /**
     * NOTE: 정산 계산 배치
     * - 오전 4시 실행
     */
    @PostMapping("/internal-batch/calculate")
    fun calculate(@RequestParam targetDate: LocalDate = LocalDate.now()): ApiResponse<Any> {
        settlementService.calculate(targetDate)
        return ApiResponse.success()
    }

    /**
     * NOTE: 정산 입금 배치
     * - 오전 9시 실행
     */
    @PostMapping("/internal-batch/transfer")
    fun transfer(): ApiResponse<Any> {
        settlementService.transfer()
        return ApiResponse.success()
    }
}
