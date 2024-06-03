package my.coupon.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("CouponServiceRedis")
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;

    /**
     * 쿠폰 생성
     * @param couponId
     * @param count
     */
    public void initializeAvailableCoupon(Long couponId, int count) {
        Coupon coupon = Coupon.builder()
                .couponId(couponId)
                .availableCount(count)
                .build();
        couponRepository.save(coupon);
    }

    /**
     * 남은 쿠폰 개수
     * @param couponId
     * @return
     */
    public int getAvailableCouponCount(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다."));
        return coupon.getAvailableCount();
    }

    /**
     * 쿠폰 발행
     * @param couponId
     */
    public synchronized void issueCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다."));
        if (coupon.getAvailableCount() > 0) {
            coupon.setAvailableCount(coupon.getAvailableCount() - 1);
            couponRepository.save(coupon);
            log.info("쿠폰 발급 완료. 남은 수량: {}", coupon.getAvailableCount());
        } else {
            throw new RuntimeException("쿠폰 발급 실패: 쿠폰이 없거나 모두 소진되었습니다.");
        }
    }
}
