package my.coupon.advanced.domain;

import jakarta.persistence.*;
import lombok.*;
import my.coupon.advanced.domain.enums.CouponType;
import my.coupon.advanced.domain.enums.CouponValue;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Enumerated(value = EnumType.STRING)
    private CouponType couponType;

    @Enumerated(value = EnumType.STRING)
    private CouponValue couponValue; // 쿠폰 가치

    @CreatedDate
    private LocalDateTime issueDate;
}
