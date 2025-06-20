package vote.dream.server;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {
    @Bean
    public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(scrf->scrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()); // ✅ 테스트에선 모든 요청 허용

        return http.build();
    }
}
