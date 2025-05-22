package vote.dream.server.global.apiPayload.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import vote.dream.server.global.apiPayload.code.BaseErrorCode;
import vote.dream.server.global.apiPayload.code.ErrorReasonDto;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"401","인증이 필요합니다."),

    _MAIL_ERROR(HttpStatus.BAD_REQUEST, "400", "인증 이메일 전송에 실패하였습니다."),
    _DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "400", "중복된 이메일입니다."),
    _DUPLICATED_LOGINID(HttpStatus.BAD_REQUEST, "400", "중복된 로그인 ID입니다."),
    _USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "400", "해당 유저를 찾을 수 없습니다."),
    _BAD_PASSWORD(HttpStatus.BAD_REQUEST, "400", "잘못된 패스워드입니다."),
    _NON_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "400", "존재하지 않는 이메일입니다."),
    _FORBIDDEN_PASSWORD(HttpStatus.FORBIDDEN, "403", "불가능한 패스워드입니다. 패스워드는 영어, 숫자 8~13글자만 가능합니다."),

    _TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 토큰을 찾을 수 없습니다."),
    _TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "401", "해당 토큰이 만료되었습니다."),
    _TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "401", "해당 토큰이 유효하지 않습니다."),

    _PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "400", "패스워드가 일치하지 않습니다."),;
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
