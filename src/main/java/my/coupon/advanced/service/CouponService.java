package my.coupon.advanced.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.coupon.advanced.domain.Coupon;
import my.coupon.advanced.domain.Issue;
import my.coupon.advanced.domain.Member;
import my.coupon.advanced.domain.enums.CouponType;
import my.coupon.advanced.domain.enums.CouponValue;
import my.coupon.advanced.dto.CouponRequest;
import my.coupon.advanced.dto.CouponResponse;
import my.coupon.advanced.repository.CouponRepository;
import my.coupon.advanced.repository.IssueRepository;
import my.coupon.advanced.repository.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CouponService{
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final String COUPON_KEY = "COUPON_CODE_";

    /**
     * 발행할 쿠폰 등록
     * @param request
     * @return
     */
    public CouponResponse addCoupon(CouponRequest request) {
        couponRepository.findByNameAndAvailableTrue(request.getCouponName())
                .ifPresent(coupon -> {
                    throw new RuntimeException("이미 동일한 쿠폰이 등록 되어있습니다.");
                });

        Coupon couponEntity = Coupon.from(request);
        Coupon savedCoupon = couponRepository.save(couponEntity);

        return CouponResponse.from(savedCoupon);
    }

    /**
     * 쿠폰 발행 서비스
     *
     * @param couponId
     */
    public void issueCoupon(Long couponId, Long memberId) {
        Coupon findCoupon = couponRepository.findByIdAndAvailableTrue(couponId)
                .orElseThrow(() -> new RuntimeException("해당 쿠폰이 발행 시간 되지 않았거나, 모두 발행 되었습니다."));
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버 정보를 찾을 수 없습니다."));

        Long remainingCoupons = getRemainingCoupons(couponId);

        if(remainingCoupons != null && remainingCoupons > 0) {
            redisTemplate.opsForValue().decrement(COUPON_KEY + couponId);

            // 마지막 쿠폰 이었을 경우 쿠폰 발행 불가로 변경
            if (remainingCoupons == 1) {
                findCoupon.updateAvailable(false);
            }

            CouponType randomCouponType = CouponType.getRandomType();
            CouponValue randomCouponValue = CouponValue.getRandomValueForType(randomCouponType);

            Issue issueEntity = Issue.builder()
                    .coupon(findCoupon)
                    .member(findMember)
                    .issueDate(LocalDateTime.now())
                    .couponType(randomCouponType)
                    .couponValue(randomCouponValue)
                    .build();

            issueRepository.save(issueEntity);

        }

    }

    /**
     * 쿠폰 발행 시작
     * @param couponId
     */
    public void startIssueCoupon(Long couponId) {
        Coupon findCoupon = couponRepository.findById(couponId)
                        .orElseThrow(() -> new RuntimeException("쿠폰 정보를 찾을 수 없습니다"));
        if(!findCoupon.isAvailable()) {
            findCoupon.updateAvailable(true); // 쿠폰 발행 가능 상태로 변경
        }

        redisTemplate.opsForValue().set(COUPON_KEY + findCoupon.getId(), String.valueOf(findCoupon.getRemain()));
    }

    /**
     * 쿠폰 개수 반환
     * @param couponId
     * @return
     */
    public Long getRemainingCoupons(Long couponId) {
        String remainingCouponsStr = (String) redisTemplate.opsForValue().get(COUPON_KEY + couponId);

        return Long.parseLong(remainingCouponsStr);
    }

    /**
     * DB 쿠폰 정보 Redis와 동기화
     * @param couponId
     */
    public void syncRedisWithDB(Long couponId) {
        Coupon findCoupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("해당 쿠폰이 없습니다"));
        Long remainingCoupons = getRemainingCoupons(couponId);
        findCoupon.updateCouponCount(remainingCoupons.intValue());
    }

    /**
     * 쿠폰 Redis <-> DB 간 동기화 스케줄러
     */
    @Scheduled(fixedRate = 60000)  // 1분마다 실행
    public void syncAllCouponsWithDB() {
        log.info("쿠폰 정보 Redis 서버와 동기화 시작합니다...");
        couponRepository.findAll().forEach(coupon -> {
            syncRedisWithDB(coupon.getId());
        });
        log.info("쿠폰 정보 Redis 서버와 동기화 되었습니다...");
    }
}
