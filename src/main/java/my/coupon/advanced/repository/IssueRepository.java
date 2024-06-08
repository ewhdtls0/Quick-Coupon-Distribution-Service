package my.coupon.advanced.repository;

import my.coupon.advanced.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("SELECT i FROM Issue i JOIN FETCH i.member JOIN FETCH i.coupon")
    List<Issue> findAllWithEntries();
}
