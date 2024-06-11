package my.coupon.advanced;

import jakarta.persistence.EntityManager;
import my.coupon.advanced.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CouponServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    CouponService couponService;
}
