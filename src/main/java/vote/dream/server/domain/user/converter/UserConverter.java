package vote.dream.server.domain.user.converter;

import org.springframework.stereotype.Component;
import vote.dream.server.domain.user.dto.request.SignUpRequestDto;
import vote.dream.server.domain.user.entity.User;

@Component
public class UserConverter {

    public static User toEntity(SignUpRequestDto request, String encoded) {
        return User.builder()
                .loginId(request.loginId())
                .password(encoded)
                .email(request.email())
                .part(request.part())
                .username(request.username())
                .team(request.team())
                .build();
    }
}
