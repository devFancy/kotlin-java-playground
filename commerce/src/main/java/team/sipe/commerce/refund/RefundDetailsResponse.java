package team.sipe.commerce.refund;


import lombok.Builder;
import lombok.Getter;

// 환불 정보 DTO
@Getter
public class RefundDetailsResponse {

    private final RefundMethodName refundMethodName;
    private final int refundAmount;
    private final RefundStatus refundStatus;

    @Builder
    public RefundDetailsResponse(final RefundMethodName refundMethodName, final int refundAmount, final RefundStatus refundStatus) {
        this.refundMethodName = refundMethodName;
        this.refundAmount = refundAmount;
        this.refundStatus = refundStatus;
    }

    public static RefundDetailsResponse from(final Refund refund) {
        return RefundDetailsResponse.builder()
                .refundMethodName(refund.getRefundMethodName())
                .refundAmount(refund.getRefundAmount())
                .refundStatus(RefundStatus.PENDING)
                .build();
    }
}
