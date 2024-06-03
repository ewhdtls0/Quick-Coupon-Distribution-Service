package my.coupon.pessimistic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceTest {

    @Autowired CouponService couponService;

    @BeforeEach
    void init() {
        couponService.initializeAvailableCoupon(10); // 10장 발급
    }

    @Test
    @DisplayName("선착순 쿠폰 발급 No Lock")
    void firstComeCouponWithNoLockTest() throws InterruptedException {
        for (int i=0; i<1000; i++) {
            new Thread(() -> {
                couponService.issueCouponNoLock(1L);
            }).start();
        }
        Thread.sleep(1000);
        /** 10장 이상 발급된다. */
    }

    @Test
    @DisplayName("선착순 쿠폰 발급 With Lock")
    void firstComeCouponWithLockTest() throws InterruptedException {
        for (int i=0; i<1000; i++) {
            new Thread(() -> {
                couponService.issueCouponWithLock(1L);
            }).start();
        }
        Thread.sleep(1000);
        /** 정확히 10장 발급된다. */
    }

}