package vote.dream.server.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReasonDto {
    private String message;
    private String code;
    private Boolean isSuccess;
    private HttpStatus httpStatus;
}
