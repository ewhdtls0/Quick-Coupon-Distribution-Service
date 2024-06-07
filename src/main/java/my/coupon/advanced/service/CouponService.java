package my.coupon.advanced.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import my.coupon.advanced.domain.Coupon;
import my.coupon.advanced.repository.CouponRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService{
    private final CouponRepository couponRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String COUPON_KEY = "COUPON_CODE_";

    /**
     * 쿠폰 발행 서비스
     * TODO: 어떤 멤버에게 어떤 형태의 쿠폰이 어떤 가치로 발행 되었는지
     * @param couponId
     * @return
     */
    @Transactional
    public boolean issueCoupon(Long couponId) {
        Coupon findCoupon = couponRepository.findByIdAndAvailableTrue(couponId)
                .orElseThrow(() -> new EntityNotFoundException("해당 쿠폰이 발행 시간 되지 않았거나, 모두 발행 되었습니다."));

        Long remainingCoupons = (Long) redisTemplate.opsForValue().get(COUPON_KEY + couponId);

        if(remainingCoupons != null && remainingCoupons > 0) {
            redisTemplate.opsForValue().decrement(COUPON_KEY + couponId);

            // 마지막 쿠폰 이었을 경우 쿠폰 발행 불가로 변경
            if (remainingCoupons == 1) {
                findCoupon.updateAvailable(false);
            }

            return true;
        }

        return false;
    }

    /**
     * 쿠폰 개수 초기화
     * @param couponId
     * @param count
     */
    @Transactional
    public void initializeCouponCount(Long couponId, int count) {
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        if(coupon.isPresent()) {
            Coupon findCoupon = coupon.get();
            findCoupon.updateCouponCount(count);
            findCoupon.updateAvailable(true);
            redisTemplate.opsForValue().set(COUPON_KEY + couponId, count);
        }
    }
}
