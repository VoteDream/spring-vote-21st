package vote.dream.server.domain.vote.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vote.dream.server.domain.vote.entity.Vote;
import vote.dream.server.domain.vote.entity.VoteType;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByType(VoteType type);
}

