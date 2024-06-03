package my.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "my.coupon.pessimistic") // 비관적 락
//@SpringBootApplication(scanBasePackages = "my.coupon.optimistic") // 낙관적 락
//@SpringBootApplication(scanBasePackages = "my.coupon.redis") // 레디스
public class CouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponApplication.class, args);
	}

}
