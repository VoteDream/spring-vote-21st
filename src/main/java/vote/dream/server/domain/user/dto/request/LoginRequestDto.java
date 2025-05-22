package vote.dream.server.domain.user.dto.request;

import vote.dream.server.domain.user.entity.Part;
import vote.dream.server.domain.user.entity.Team;

public record LoginRequestDto(
        String loginId,
        String password

) {
}
