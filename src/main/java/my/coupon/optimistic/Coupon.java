package my.coupon.optimistic;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity(name = "CouponOptimistic")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "coupon_optimistic")
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("발급 가능 수")
    private int availableCount;

    @Version
    @Comment("버전")
    private Long version;

    /** 쿠폰 발급 수량 수정 */
    public void updateCouponCount(int count) {
        this.availableCount = count;
    }
}
