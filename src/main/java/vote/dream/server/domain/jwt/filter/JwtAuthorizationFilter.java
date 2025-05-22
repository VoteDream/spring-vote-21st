package vote.dream.server.domain.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import vote.dream.server.domain.jwt.Custom.CustomDetails;
import vote.dream.server.domain.jwt.Custom.CustomDetailsService;
import vote.dream.server.domain.user.entity.User;
import vote.dream.server.domain.user.repository.UserRepository;
import vote.dream.server.global.apiPayload.ApiResponse;

import java.io.IOException;
import java.security.SignatureException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtAuthorizationFilter: 인증 시작");

        try {
            String accessToken = jwtUtil.resolveAccessToken(request);

            // access token 없이 접근 시
            if(accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 유효성 검사
            jwtUtil.validationToken(accessToken);

            // accessToken 을 기반으로 principalDetail 저장
            String loginId = jwtUtil.getLoginId(accessToken);

            User user = userRepository.findByLoginId(loginId)
                    .orElseThrow(()->new RuntimeException("User not found"));

            CustomDetails principalDetails = new CustomDetails(user);



            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    principalDetails,
                    null,
                    principalDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            try {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");

                ApiResponse<?> apiResponse = ApiResponse.onFailure(
                        String.valueOf(HttpStatus.UNAUTHORIZED),
                        "Access token expired",
                        null
                );

                ObjectMapper om = new ObjectMapper();
                om.writeValue(response.getWriter(), apiResponse);
            } catch (IOException ex) {
                log.error("IOException occured while setting error response", ex);
            }
            log.warn("[*] case : accessToken Expired");
        } catch (SignatureException e) {
            log.info("[*] case : accessToken SignatureException");
        }

    }
}
