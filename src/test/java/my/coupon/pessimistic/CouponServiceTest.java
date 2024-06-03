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
        couponService.initializeAvailableCoupon(10);
    }

    @Test
    @DisplayName("선착순 쿠폰 발급 No Lock")
    void firstComeCouponWithNoLockTest() throws InterruptedException {
        for (int i=0; i<1000; i++) {
            new Thread(() -> {
                try {
                    couponService.issueCouponNoLock(1L);
                } catch (Exception e) {
                    System.out.println("e = " + e);
                }
            }).start();
        }
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("선착순 쿠폰 발급 With Lock")
    void firstComeCouponWithLockTest() throws InterruptedException {
        for (int i=0; i<1000; i++) {
            new Thread(() -> {
                try {
                    couponService.issueCouponWithLock(1L);
                } catch (Exception e) {
                    System.out.println("e = " + e.getMessage());
                }
            }).start();
        }
        Thread.sleep(1000);
    }

}