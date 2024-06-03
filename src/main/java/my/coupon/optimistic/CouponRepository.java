package my.coupon.optimistic;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("CouponRepositoryOptimistic")
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.OPTIMISTIC) // 낙관적 락
    @Query("SELECT c FROM CouponOptimistic c WHERE c.id = :couponId")
    Optional<Coupon> findByIdWithLock(@Param("couponId") Long couponId);
}
