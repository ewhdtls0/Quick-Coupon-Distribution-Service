package my.coupon.pessimistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("CouponServicePessimistic")
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;

    /**
     * 쿠폰 발급 수량 설정
     * @param count
     */
    @Transactional
    public void initializeAvailableCoupon(int count) {
        Coupon initCouponEntity = Coupon.builder()
                .availableCount(count)
                .build();

        couponRepository.save(initCouponEntity);
    }

    /**
     * 남은 쿠폰 수량 조회
     * @param couponId
     * @return
     */
    @Transactional(readOnly = true)
    public int getAvailableCouponCount(Long couponId) {
        Coupon findCoupon = couponRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new RuntimeException("대상 쿠폰이 없습니다."));
        
        return findCoupon.getAvailableCount();
    }

    /**
     * 쿠폰 발급 - 락
     *
     * @param couponId
     */
    @Transactional
    public void issueCouponWithLock(Long couponId) {
        synchronized (this) {
            Coupon findCoupon = couponRepository.findByIdWithLock(couponId)
                    .orElseThrow(() -> new RuntimeException("대상 쿠폰이 없습니다."));
            issueCoupon(findCoupon);
        }

    }

    /**
     * 쿠폰 발급 - No 락
     * @param couponId
     */
    @Transactional
    public void issueCouponNoLock(Long couponId) {
        synchronized (this) {
            Coupon findCoupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new RuntimeException("대상 쿠폰이 없습니다."));
            issueCoupon(findCoupon);
        }

    }

    private void issueCoupon(Coupon findCoupon) {
        if (findCoupon.getAvailableCount() > 0) {
            findCoupon.updateCouponCount(findCoupon.getAvailableCount() - 1); // 쿠폰 발급 수량 차감
            couponRepository.save(findCoupon);
            log.info("쿠폰 남은 수량 : " + findCoupon.getAvailableCount());
        }
    }

}
