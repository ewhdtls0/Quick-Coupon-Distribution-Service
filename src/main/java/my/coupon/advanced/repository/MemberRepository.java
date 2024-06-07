package my.coupon.advanced.repository;

import my.coupon.advanced.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
