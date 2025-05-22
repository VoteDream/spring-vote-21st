package vote.dream.server.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorReasonDto {
    private String message;
    private String code;
    private Boolean isSuccess;
    private HttpStatus httpStatus;
}
