package vote.dream.server.global.apiPayload.status;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import vote.dream.server.global.apiPayload.code.BaseCode;
import vote.dream.server.global.apiPayload.code.ReasonDto;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "생성되었습니다."),
    _UPDATED(HttpStatus.ACCEPTED, "COMMON202", "수정되었습니다."),
    _DELETED(HttpStatus.ACCEPTED, "COMMON204", "삭제되었습니다."),;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason(){
        return ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build()
                ;
    }

    @Override
    public ReasonDto getReasonHttpStatus(){
        return ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }

}
