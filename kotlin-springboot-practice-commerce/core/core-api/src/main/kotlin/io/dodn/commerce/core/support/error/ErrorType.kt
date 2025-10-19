package io.dodn.commerce.core.support.error

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(val status: HttpStatus, val code: ErrorCode, val message: String, val logLevel: LogLevel) {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", LogLevel.ERROR),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, ErrorCode.E400, "요청이 올바르지 않습니다.", LogLevel.INFO),
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, ErrorCode.E401, "해당 데이터를 찾을 수 없습니다.", LogLevel.ERROR),

    // 주문
    ORDER_ALREADY_PAID(HttpStatus.BAD_REQUEST, ErrorCode.E1000, "이미 결제가 완료된 주문입니다.", LogLevel.INFO),

    // 결제
    PAYMENT_INVALID_STATE(HttpStatus.BAD_REQUEST, ErrorCode.E2000, "결제 상태가 유효하지 않습니다.", LogLevel.INFO),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, ErrorCode.E2001, "결제 금액이 일치하지 않습니다.", LogLevel.INFO),
    PAYMENT_INVALID_AMOUNT(HttpStatus.BAD_REQUEST, ErrorCode.E2002, "결제 금액이 0보다 작을 수 없습니다.", LogLevel.INFO),

    // 상품
    PRODUCT_MISMATCH_IN_ORDER(HttpStatus.BAD_REQUEST, ErrorCode.E3000, "요청한 상품 정보와 일치하지 않습니다.", LogLevel.INFO),

    // 쿠폰
    COUPON_NOT_FOUND_OR_EXPIRED(HttpStatus.BAD_REQUEST, ErrorCode.E4000, "쿠폰을 찾을 수 없거나 만료되었습니다.", LogLevel.INFO),
    COUPON_ALREADY_DOWNLOADED(HttpStatus.BAD_REQUEST, ErrorCode.E4001, "이미 다운로드한 쿠폰입니다.", LogLevel.INFO),

    // 소유 쿠폰
    OWNED_COUPON_INVALID(HttpStatus.BAD_REQUEST, ErrorCode.E5000, "사용할 수 없는 쿠폰입니다.", LogLevel.INFO),

    // 포인트
    POINT_EXCEEDS_BALANCE(HttpStatus.BAD_REQUEST, ErrorCode.E6000, "보유 포인트를 초과하여 사용할 수 없습니다.", LogLevel.INFO),

    // 리뷰
    REVIEW_HAS_NOT_ORDER(HttpStatus.BAD_REQUEST, ErrorCode.E7000, "리뷰 작성 가능한 주문이 없습니다.", LogLevel.INFO),
    REVIEW_ALREADY_REVIEWED(HttpStatus.BAD_REQUEST, ErrorCode.E7001, "이미 리뷰를 작성한 상품입니다.", LogLevel.INFO),
    REVIEW_UPDATE_EXPIRED(HttpStatus.BAD_REQUEST, ErrorCode.E7002, "리뷰를 수정 기간이 만료 되었습니다.", LogLevel.INFO),
}
