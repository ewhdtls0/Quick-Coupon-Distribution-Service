package my.coupon.advanced.domain;

import jakarta.persistence.*;
import lombok.Getter;
import my.coupon.advanced.domain.enums.CouponType;

@Entity
@Getter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CouponType couponType;

    private int remain; // 남은 수량

    private boolean available;
}
