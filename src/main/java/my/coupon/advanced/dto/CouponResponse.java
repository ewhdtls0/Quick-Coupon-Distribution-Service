package my.coupon.advanced.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.coupon.advanced.domain.Coupon;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {
    private Long couponId;
    private String couponName;
    private int remain;
    private boolean available;

    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .couponId(coupon.getId())
                .couponName(coupon.getName())
                .remain(coupon.getRemain())
                .available(coupon.isAvailable())
                .build();
    }
}
