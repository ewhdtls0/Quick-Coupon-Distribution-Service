package my.coupon.advanced;

import my.coupon.advanced.dto.CouponRequest;
import my.coupon.advanced.dto.CouponResponse;
import my.coupon.advanced.dto.IssueResponse;
import my.coupon.advanced.dto.MemberRequest;
import my.coupon.advanced.service.CouponService;
import my.coupon.advanced.service.IssueService;
import my.coupon.advanced.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CouponRedisIssueServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    CouponService couponService;
    @Autowired
    IssueService issueService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void clear() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            redisTemplate.delete(keys); // 레디스 키 비우기
        }
    }

    @Test
    @DisplayName("쿠폰 멀티쓰레드 상황에서 원자적 발행된다")
    void issueCouponOnMultiThreadTest() throws InterruptedException, ExecutionException {
        for(int i=0; i<1000; i++) {
            MemberRequest request = MemberRequest.builder()
                    .account("test" + i)
                    .password("test" + i)
                    .build();

            memberService.addMember(request);
        }

        CouponRequest request = CouponRequest.builder()
                .couponName("[6월] 할인 쿠폰")
                .remain(50)
                .available(false)
                .build();

        CouponResponse response = couponService.addCoupon(request);
        Thread.sleep(3000); // 3초뒤 쿠폰 발급 시작

        couponService.startIssueCoupon(response.getCouponId());

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {
            Long memberId = (long) i;
            futures.add(executor.submit(() -> {
                try{
                    couponService.issueCoupon(response.getCouponId(),  memberId);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }));
        }

        // 모든 작업이 완료될 때까지 대기
        for (Future<?> future : futures) {
            future.get(); // 작업이 완료될 때까지 대기
        }

        executor.shutdown(); // 쓰레드 풀 종료

        List<IssueResponse> allIssue = issueService.findAllIssue();

        assertThat(allIssue).hasSize(request.getRemain());
    }
}
