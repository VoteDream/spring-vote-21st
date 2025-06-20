package vote.dream.server.global.apiPayload.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import vote.dream.server.global.apiPayload.code.BaseErrorCode;
import vote.dream.server.global.apiPayload.code.ErrorReasonDto;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 500 서버 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 에러, 관리자에게 문의 바랍니다."),

    // 400 클라이언트 에러
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"400","잘못된 요청입니다."),
    _DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "400", "중복된 이메일입니다."),
    _DUPLICATED_LOGIN_ID(HttpStatus.BAD_REQUEST, "400", "중복된 로그인 아이디입니다."),
    _USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "400", "아이디와 일치하는 계정이 존재하지 않습니다"),
    _BAD_PASSWORD(HttpStatus.BAD_REQUEST, "400", "비밀번호가 일치하지 않습니다."),


    // 401 인증 에러
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"401","인증이 필요합니다."),
    _TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "401", "해당 토큰이 만료되었습니다."),
    _TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "401", "해당 토큰이 유효하지 않습니다."),

    // 403 권한 에러

    // 404 리소스 에러
    _TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "토큰이 없습니다."),;

    // 409 충돌 에러

    // 기타 에러
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
