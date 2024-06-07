package my.coupon.advanced.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.coupon.advanced.domain.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    private Long memberId;
    private String account;
    private String password;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .memberId(member.getId())
                .account(member.getAccount())
                .password(member.getPassword())
                .build();
    }
}
