package vote.dream.server.domain.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vote.dream.server.domain.vote.entity.Team;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);
}