package vote.dream.server.domain.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vote.dream.server.domain.jwt.dto.JwtDto;

import vote.dream.server.domain.jwt.filter.JwtUtil;
import vote.dream.server.domain.user.dto.request.LoginRequestDto;
import vote.dream.server.domain.user.dto.request.SignUpRequestDto;
import vote.dream.server.domain.user.entity.User;
import vote.dream.server.domain.user.service.AuthService;
import vote.dream.server.global.apiPayload.ApiResponse;
import vote.dream.server.global.apiPayload.exception.GeneralException;
import vote.dream.server.global.apiPayload.status.ErrorStatus;
import vote.dream.server.global.apiPayload.status.SuccessStatus;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody LoginRequestDto request,
        HttpServletResponse response) {
        Map<JwtDto,User> result= authService.login(request.loginId(), request.password());

        JwtDto jwt = result.keySet().iterator().next(); // JwtDto
        User user = result.get(jwt);

        // 쿠키 설정
        Cookie cookie = new Cookie("refreshToken", jwt.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);         // HTTPS 환경일 때 true
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(cookie);

        // 응답 바디
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("accessToken", jwt.accessToken());
        responseBody.put("user", user);

        return ApiResponse.onSuccess(responseBody);
    }

    @PostMapping("/register")
    public ApiResponse<?> signUp(@RequestBody SignUpRequestDto request) {
        authService.register(request);
        return ApiResponse.onSuccess(SuccessStatus._CREATED);
    }

    @GetMapping("/check")
    public ApiResponse<?> checkId(@RequestParam String loginId){
        authService.checkLoginId(loginId);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @PostMapping("/reissue")
    public ApiResponse<?> reissueToken(HttpServletRequest request, HttpServletResponse response) throws SignatureException {
        String refreshToken = extractRefreshFromCookie(request);

        return ApiResponse.onSuccess(authService.checkRefreshToken(refreshToken));

    }

    private String extractRefreshFromCookie(HttpServletRequest request) {
        if(request.getCookies() == null)
            return null;

        for(Cookie cookie : request.getCookies()) {
            if("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
