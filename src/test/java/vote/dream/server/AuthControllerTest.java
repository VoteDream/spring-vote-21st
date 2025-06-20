package vote.dream.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import vote.dream.server.domain.jwt.dto.JwtDto;
import vote.dream.server.domain.jwt.filter.JwtUtil;
import vote.dream.server.domain.user.controller.UserController;
import vote.dream.server.domain.user.dto.request.LoginRequestDto;
import vote.dream.server.domain.user.dto.request.SignUpRequestDto;
import vote.dream.server.domain.user.entity.Part;
import vote.dream.server.domain.user.entity.Team;
import vote.dream.server.domain.user.entity.User;
import vote.dream.server.domain.user.service.AuthService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;


@WebMvcTest(UserController.class)
@Import({MockConfig.class, TestSecurityConfig.class})
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 성공 - accessToken 반환 & refreshToken 쿠키")
    void login_success() throws Exception {
        // given
        String accessToken = "access.token";
        String refreshToken = "refresh.token";

        JwtDto jwtDto = new JwtDto(accessToken, refreshToken);
        User user = User.builder()
                .id(1L)
                .loginId("testuser")
                .email("example@gmail.com")
                .password("1234")
                .username("test")
                .team(Team.DEARDREAM)
                .part(Part.BACKEND)
                .build();

        Map<JwtDto, User> result = new HashMap<>();
        result.put(jwtDto, user);

        LoginRequestDto loginRequest = new LoginRequestDto("testuser", "password");

        when(authService.login(any(), any())).thenReturn(result);

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.accessToken").value(accessToken))
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(cookie().httpOnly("refreshToken", true));
    }

    @Test
    @DisplayName("회원가입 성공")
    void register_success() throws Exception {
        SignUpRequestDto signUpRequest = new SignUpRequestDto("testuser", "password",  "example@gmail.com",
                Part.BACKEND, "test", Team.DEARDREAM);

        /*
        id(1L)
                .loginId("testuser")
                .email("example@gmail.com")
                .password("1234")
                .username("test")
                .team(Team.DEARDREAM)
                .part(Part.BACKEND)
         */
        // void method라서 doNothing 필요 없음

        mockMvc.perform(post("/api/v1/users/register").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200")) // ✅ 존재하는 key
                .andExpect(jsonPath("$.result").value("_CREATED"));
    }

    @Test
    @DisplayName("아이디 중복 확인 성공")
    void check_login_id() throws Exception {
        String loginId = "testuser";

        mockMvc.perform(get("/api/v1/users/check")
                        .with(csrf())
                        .param("loginId", loginId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.result").value("_OK"));
    }

    @Test
    @DisplayName("refreshToken 재발급 성공")
    void reissue_token_success() throws Exception {
        String newAccessToken = "new.access.token";

        when(authService.checkRefreshToken(any())).thenReturn(newAccessToken);

        mockMvc.perform(post("/api/v1/users/reissue")
                        .with(csrf())
                        .cookie(new Cookie("refreshToken", "valid.refresh.token")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(newAccessToken));
    }
}
