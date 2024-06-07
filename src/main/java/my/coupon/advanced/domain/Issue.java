package my.coupon.advanced.domain;

import jakarta.persistence.*;
import lombok.Getter;
import my.coupon.advanced.domain.enums.CouponType;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
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

    @CreatedDate
    private LocalDateTime issueDate;
}
