package my.coupon.advanced.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import my.coupon.advanced.dto.MemberRequest;

@Entity
@Getter
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account;
    private String password;

    public static Member from(MemberRequest request) {
        return Member.builder()
                .account(request.getAccount())
                .password(request.getPassword())
                .build();
    }
}
