package vote.dream.server;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import vote.dream.server.domain.jwt.filter.JwtUtil;
import vote.dream.server.domain.user.service.AuthService;

@TestConfiguration
public class MockConfig {
    @Bean
    public AuthService authService() {
        return Mockito.mock(AuthService.class);
    }

    @Bean
    public JwtUtil jwtUtil() {
        return Mockito.mock(JwtUtil.class);
    }
}
