package my.coupon.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceTest {
    @Autowired
    private CouponService couponService;

    @BeforeEach
    void init() {
        couponService.initializeAvailableCoupon(1L, 10);
    }

    @Test
    @DisplayName("쿠폰 발급 레디스 테스트")
    void issueCouponTest() {
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                couponService.issueCoupon(1L);
            }).start();
        }
        /** 많은 트래픽 상황에서도 정확히 10장 발급된다.. */
    }
}