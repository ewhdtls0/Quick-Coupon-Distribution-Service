package my.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages={"my.coupon.advanced"})
public class CouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponApplication.class, args);
	}

//	@Bean
//	public RedisTemplate<String, Coupon> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//		RedisTemplate<String, Coupon> template = new RedisTemplate<>();
//		template.setConnectionFactory(redisConnectionFactory);
//		template.setKeySerializer(new StringRedisSerializer());
//		template.setValueSerializer(new GenericToStringSerializer<>(Coupon.class));
//		return template;
//	}
}
