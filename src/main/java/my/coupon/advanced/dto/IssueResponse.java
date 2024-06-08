package my.coupon.advanced.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.coupon.advanced.domain.Issue;
import my.coupon.advanced.domain.enums.CouponType;
import my.coupon.advanced.domain.enums.CouponValue;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueResponse {
    private Long issueId;
    private Long memberId;
    private CouponType couponType;
    private CouponValue couponValue;
    private LocalDateTime issuedDate;

    public static IssueResponse from(Issue issue) {
        return IssueResponse.builder()
                .issueId(issue.getId())
                .memberId(issue.getMember().getId())
                .couponType(issue.getCouponType())
                .couponValue(issue.getCouponValue())
                .issuedDate(issue.getIssueDate())
                .build();
    }
}
