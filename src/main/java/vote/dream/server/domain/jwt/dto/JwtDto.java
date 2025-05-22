package vote.dream.server.domain.jwt.dto;

public record JwtDto(
        String accessToken,
        String refreshToken
) {
}
