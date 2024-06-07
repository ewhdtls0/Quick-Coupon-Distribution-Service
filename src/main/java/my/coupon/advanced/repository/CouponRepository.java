package my.coupon.advanced.repository;

import my.coupon.advanced.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId AND c.available = true")
    Optional<Coupon> findByIdAndAvailableTrue(@Param("couponId") Long couponId);
}
