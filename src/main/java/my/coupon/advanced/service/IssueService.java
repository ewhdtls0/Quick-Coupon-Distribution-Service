package my.coupon.advanced.service;

import lombok.RequiredArgsConstructor;
import my.coupon.advanced.dto.IssueResponse;
import my.coupon.advanced.repository.IssueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;

    /**
     * 쿠폰 발행 정보 리스트 조회
     * @return
     */
    public List<IssueResponse> findAllIssue() {
        return issueRepository.findAllWithEntries()
                .stream().map(IssueResponse::from).toList();
    }

}
