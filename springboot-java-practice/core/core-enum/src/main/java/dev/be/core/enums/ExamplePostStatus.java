package dev.be.core.enums;

/**
 *  [주의]
 *  이 Enum 은 API 계층에 직접 노출되지 않으며,
 *  외부 노출 시에는 반드시 String 형태로 변환해 사용해야 합니다.
 *  예: examplePostStatus.name().toLowerCase()
 */
public enum ExamplePostStatus {
    HOLDING, CANCELLED, COMPLETED
}
