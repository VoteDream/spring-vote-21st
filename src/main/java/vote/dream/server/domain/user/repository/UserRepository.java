package vote.dream.server.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vote.dream.server.domain.user.entity.Part;
import vote.dream.server.domain.user.entity.Team;
import vote.dream.server.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);

    // 이름 + 파트 + 팀 이 모두 같을 수는 없음
    boolean existsByUsernameAndPartAndTeam (String username, Part part, Team team);

}
