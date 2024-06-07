package my.coupon.advanced.service;

import lombok.RequiredArgsConstructor;
import my.coupon.advanced.domain.Member;
import my.coupon.advanced.dto.MemberRequest;
import my.coupon.advanced.dto.MemberResponse;
import my.coupon.advanced.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 멤버 등록
     * @param request
     * @return
     */
    @Transactional
    public MemberResponse addMember(MemberRequest request) {
        Member memberEntity = Member.from(request);

        Member savedMember = memberRepository.save(memberEntity);

        return MemberResponse.from(savedMember);
    }
}
