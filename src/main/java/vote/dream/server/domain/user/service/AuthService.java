package vote.dream.server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vote.dream.server.domain.jwt.Custom.CustomDetails;
import vote.dream.server.domain.jwt.dto.JwtDto;
import vote.dream.server.domain.jwt.filter.JwtUtil;
import vote.dream.server.domain.user.converter.UserConverter;
import vote.dream.server.domain.user.dto.request.SignUpRequestDto;
import vote.dream.server.domain.user.entity.User;
import vote.dream.server.domain.user.repository.UserRepository;
import vote.dream.server.global.apiPayload.exception.GeneralException;
import vote.dream.server.global.apiPayload.status.ErrorStatus;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // 로그인 로직
    public JwtDto login(String loginId, String password) {
        User newUser = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        if(!passwordEncoder.matches(password, newUser.getPassword())) {
            throw new GeneralException(ErrorStatus._PASSWORD_NOT_MATCH);
        }

        // CustomDetails 생성
        CustomDetails customDetails = new CustomDetails(newUser);

        // JWT 발급
        String accessToken = jwtUtil.createJwtAccessToken(customDetails);
        String refreshToken = jwtUtil.createJwtRefreshToken(customDetails);

        return new JwtDto(accessToken, refreshToken);
    }

    // 회원가입 로직
    // 자동 로그인 x
    public void register(SignUpRequestDto request) {
        // 로그인 아이디 중복 체크
        if(userRepository.existsByLoginId(request.loginId())) {
            throw new GeneralException(ErrorStatus._DUPLICATED_LOGIN_ID);
        }

        // 이메일 중복 체크
        if(userRepository.existsByEmail(request.email())) {
            throw new GeneralException(ErrorStatus._DUPLICATED_EMAIL);
        }

        String encoded = passwordEncoder.encode(request.password());

        User user = UserConverter.toEntity(request, encoded);

        userRepository.save(user);

    }


}
