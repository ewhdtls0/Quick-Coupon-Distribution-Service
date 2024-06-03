package my.coupon.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Coupon")
@Builder
public class Coupon {
    @Id
    private Long couponId;
    private int availableCount;
}
