package my.coupon.pessimistic;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "coupon_pessimistic")
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("발급 가능 수")
    private int availableCount;

    /** 쿠폰 발급 수량 수정 */
    public void updateCouponCount(int count) {
        this.availableCount = count;
    }
}
