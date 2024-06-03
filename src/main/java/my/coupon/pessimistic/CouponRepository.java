package my.coupon.pessimistic;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("CouponRepositoryPessimistic")
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // 쓰기 잠금
    @Query("SELECT c FROM CouponPessimistic c WHERE c.id = :couponId")
    Optional<Coupon> findByIdWithLock(@Param("couponId") Long couponId);
}
