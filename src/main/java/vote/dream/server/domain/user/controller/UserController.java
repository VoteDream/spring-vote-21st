package vote.dream.server.domain.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vote.dream.server.domain.jwt.dto.JwtDto;

import vote.dream.server.domain.user.dto.request.LoginRequestDto;
import vote.dream.server.domain.user.dto.request.SignUpRequestDto;
import vote.dream.server.domain.user.service.AuthService;
import vote.dream.server.global.apiPayload.ApiResponse;
import vote.dream.server.global.apiPayload.status.SuccessStatus;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<JwtDto> login(@RequestBody LoginRequestDto request) {
        JwtDto jwtDto = authService.login(request.loginId(), request.password());
        return ApiResponse.onSuccess(jwtDto);
    }

    @PostMapping("/register")
    public ApiResponse<?> signUp(@RequestBody SignUpRequestDto request) {
        authService.register(request);
        return ApiResponse.onSuccess(SuccessStatus._CREATED);
    }
}
