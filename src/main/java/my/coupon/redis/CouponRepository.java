package my.coupon.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("CouponRepositoryRedis")
public interface CouponRepository extends CrudRepository<Coupon, Long> {
}
