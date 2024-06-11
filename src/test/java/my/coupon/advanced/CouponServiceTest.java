package my.coupon.advanced;

import jakarta.persistence.EntityManager;
import my.coupon.advanced.domain.Member;
import my.coupon.advanced.dto.CouponRequest;
import my.coupon.advanced.dto.CouponResponse;
import my.coupon.advanced.service.CouponService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class CouponServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    CouponService couponService;

    Member member = null;
    @BeforeEach
    void init() {
        Member member = Member.builder()
                .account("memberA")
                .password("password1")
                .build();

        em.persist(member); // 멤버 등록
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("쿠폰 등록된다.")
    void addCouponTest() {
        CouponRequest request = CouponRequest.builder()
                .couponName("[6월] 할인 쿠폰")
                .remain(100)
                .available(true)
                .build();

        CouponResponse response = couponService.addCoupon(request);

        assertThat(response.getCouponName()).isEqualTo("[6월] 할인 쿠폰");
        assertThat(response.getRemain()).isEqualTo(100);
    }

    @Test
    @DisplayName("동일 쿠폰 등록 안된다.")
    void addAlreadyExistCouponTest() {
        CouponRequest request = CouponRequest.builder()
                .couponName("[6월] 할인 쿠폰")
                .remain(100)
                .available(true)
                .build();

        couponService.addCoupon(request);

        assertThatThrownBy(() -> {
            CouponRequest request2 = CouponRequest.builder()
                    .couponName("[6월] 할인 쿠폰")
                    .remain(100)
                    .available(true)
                    .build();

            couponService.addCoupon(request2);
        });
    }
}
