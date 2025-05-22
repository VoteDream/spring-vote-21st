package vote.dream.server.domain.user.dto.request;

import vote.dream.server.domain.user.entity.Part;
import vote.dream.server.domain.user.entity.Team;

public record SignUpRequestDto(
        String loginId,
        String password,
        String email,
        Part part,
        String username,
        Team team
) {

}
