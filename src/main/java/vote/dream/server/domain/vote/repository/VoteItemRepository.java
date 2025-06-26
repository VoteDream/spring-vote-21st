package vote.dream.server.domain.vote.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import vote.dream.server.domain.vote.entity.VoteItem;

import java.util.List;

public interface VoteItemRepository extends JpaRepository<VoteItem, Long> {
    List<VoteItem> findByVoteId(Long voteId);
    List<VoteItem> findByVoteId(Long voteId, Sort sort);
}
