package my.coupon.optimistic;

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
    @DisplayName("No lock 낙관적 락 방식 선착순 쿠폰 발급 테스트")
    void firstComeCouponNoOptimisticTest() throws InterruptedException {
        for (int i=0; i<1000; i++) {
            new Thread(() -> {
                try {
                    couponService.issueCouponNoLock(1L);
                } catch (Exception e) {
                    System.out.println("e.getMessage() = " + e.getMessage());
                }
            }).start();
        }
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("lock 낙관적 락 방식 선착순 쿠폰 발급 테스트")
    void firstComeCouponOptimisticTest() throws InterruptedException {
        for (int i=0; i<100; i++) {
            new Thread(() -> {
                try {
                    couponService.issueCouponLock(1L);
                } catch (Exception e) {
                    System.out.println("e.getMessage() = " + e.getMessage());
                }
            }).start();
        }
        Thread.sleep(1000);
        /** 대량 트래픽에서는 적합하지 않아보임.. 버전 충돌로 인한 예외 관리는 가능하겠지만 ..*/
    }

}