package my.coupon.advanced;

import jakarta.persistence.EntityManager;
import my.coupon.advanced.domain.Coupon;
import my.coupon.advanced.domain.Issue;
import my.coupon.advanced.domain.Member;
import my.coupon.advanced.dto.CouponRequest;
import my.coupon.advanced.dto.CouponResponse;
import my.coupon.advanced.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
@TestPropertySource(properties = "=false")
public class CouponServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    CouponService couponService;

    Member member;
    @BeforeEach
    void init() {
        member = Member.builder()
                .account("memberA")
                .password("password1")
                .build();

        em.persist(member); // 멤버 등록
        em.flush();
        em.clear();
    }

//    @AfterEach
//    void clearRedis() {
//        Set<String> keys = redisTemplate.keys("*");
//        if (keys != null) {
//            redisTemplate.delete(keys); // 레디스 키 비우기
//        }
//
//    }

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

    @Test
    @DisplayName("쿠폰 발행 시작한다.")
    void startIssueCouponTest() {
        Coupon coupon = Coupon.builder()
                .available(false)
                .name("[6월] 할인 쿠폰")
                .remain(100)
                .build();

        em.persist(coupon);
        em.flush();
        em.clear();

        couponService.startIssueCoupon(coupon.getId());

        Coupon findCoupon = em.find(Coupon.class, coupon.getId());

        assertThat(findCoupon.isAvailable()).isEqualTo(true);
    }

    @Test
    @DisplayName("쿠폰 발행한다.")
    void issueCouponTest() {
        Coupon coupon = Coupon.builder()
                .available(false)
                .name("[6월] 할인 쿠폰")
                .remain(100)
                .build();

        em.persist(coupon);
        em.flush();
        em.clear();

        couponService.startIssueCoupon(coupon.getId());
        couponService.issueCoupon(coupon.getId(), member.getId());

        Issue issue = em.find(Issue.class, 1L);

        assertThat(redisTemplate.opsForValue().get("COUPON_CODE_1")).isEqualTo("99");
        assertThat(issue.getCoupon().getId()).isEqualTo(coupon.getId());
        assertThat(issue.getMember().getId()).isEqualTo(member.getId());

        System.out.println("coupon type = " + issue.getCouponType());
        System.out.println("coupon value = " + issue.getCouponValue());
    }

    @Rollback(value = false)
    @Test
    @DisplayName("쿠폰 멀티쓰레드 상황에서 원자적 발행된다")
    void issueCouponOnMultiThreadTest() {
        Coupon coupon = Coupon.builder()
                .available(false)
                .name("[6월] 할인 쿠폰")
                .remain(10)
                .build();

        em.persist(coupon);
        em.flush();
        em.clear();

        couponService.startIssueCoupon(coupon.getId());
        Coupon findCoupon = em.find(Coupon.class, coupon.getId());

        assertThat(findCoupon.isAvailable()).isEqualTo(true);

        for(int i=0; i<100; i++) {
            Member member = Member.builder()
                    .account("test" + i)
                    .password("test" + i)
                    .build();

            em.persist(member);
        }
        em.flush();
        em.clear();

        /** Before Each의 init 데이터 제외 */
        for (int i = 1; i < 100; i++) {
            int finalI = i;
            new Thread(() -> {
                try{
                    couponService.issueCoupon(findCoupon.getId(), (long) finalI);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }).start();
        }
    }
}
