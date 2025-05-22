package vote.dream.server.domain.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vote.dream.server.domain.jwt.Custom.CustomDetails;
import vote.dream.server.domain.jwt.dto.JwtDto;
import vote.dream.server.domain.user.dto.request.LoginRequestDto;
import vote.dream.server.global.apiPayload.ApiResponse;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // login 요청 시 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        log.info("JwtAuthenticationFilter: 로그인 시도 중");
        // 1. username, password 받아서
        ObjectMapper om =new ObjectMapper();
        LoginRequestDto loginRequestDTO;
        try {
            loginRequestDTO = om.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            log.error("LoginRequestDto parsing error", e);
            throw new AuthenticationServiceException("Invalid login request format.");
        }

        // usernamePassword Token generate
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.loginId(),
                        loginRequestDTO.password()
                );


        // 인증 완료 시 successfulAuthentication
        // 인증 실패 시 unsuccessfulAuthentication
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException, ServletException {
        CustomDetails principalDetails = (CustomDetails) authentication.getPrincipal();

        log.info("[*] Login Success - Login with" + principalDetails.getUsername());
        JwtDto jwtDTO = new JwtDto(
                jwtUtil.createJwtAccessToken(principalDetails),
                jwtUtil.createJwtRefreshToken(principalDetails)
        );

        log.info("Access token: " + jwtDTO.accessToken());
        log.info("Refresh token: " + jwtDTO.refreshToken());

        ApiResponse<JwtDto> apiResponse = ApiResponse.onSuccess(jwtDTO);

        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType("application/json;charset=UTF-8");

        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(), apiResponse);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.warn("[!] Login failed: " + failed.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<?> errorResponse = ApiResponse.onFailure(
                String.valueOf(HttpStatus.UNAUTHORIZED),
                "로그인 실패: " + failed.getMessage(),
                null
        );

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }

}
