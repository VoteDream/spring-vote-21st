package vote.dream.server.global.apiPayload.exception;

import lombok.Getter;
import vote.dream.server.global.apiPayload.code.BaseErrorCode;
import vote.dream.server.global.apiPayload.code.ErrorReasonDto;

@Getter
public class GeneralException extends RuntimeException{
    private BaseErrorCode code;

    public GeneralException(String message) {
        super(message);
        this.code = null;
    }

    public GeneralException(BaseErrorCode code){
        super(code.getReason().getMessage());
        this.code = code;
    }

    public ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
