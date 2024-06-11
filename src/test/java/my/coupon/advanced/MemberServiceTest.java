package my.coupon.advanced;

import jakarta.persistence.EntityManager;
import my.coupon.advanced.dto.MemberRequest;
import my.coupon.advanced.dto.MemberResponse;
import my.coupon.advanced.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("멤버 등록된다.")
    void addMemberTest() {
        MemberRequest request = MemberRequest.builder()
                .account("memberA")
                .password("password1")
                .build();

        MemberResponse response = memberService.addMember(request);

        assertThat(response.getAccount()).isEqualTo("memberA");
        assertThat(response.getPassword()).isEqualTo("password1");
    }
}
