package my.coupon.advanced.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import my.coupon.advanced.dto.CouponRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int remain; // 남은 수량

    private boolean available;

    public static Coupon from(CouponRequest request) {
        return Coupon.builder()
                .remain(request.getRemain())
                .available(request.isAvailable())
                .build();
    }

    /** 쿠폰 수량 변경 */
    public void updateCouponCount(int remain) {
        this.remain = remain;
    }

    /** 쿠폰 발행 가능 여부 변경 */
    public void updateAvailable(boolean status) {
        this.available = status;
    }
}
