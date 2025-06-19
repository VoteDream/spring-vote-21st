package vote.dream.server.domain.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vote.dream.server.domain.vote.entity.VoteRecord;

public interface VoteRecordRepository extends JpaRepository<VoteRecord, Long> {
    boolean existsByUserIdAndVoteId(Long userId, Long voteId);
}
