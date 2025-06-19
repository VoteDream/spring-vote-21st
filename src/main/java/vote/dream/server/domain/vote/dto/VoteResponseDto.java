package vote.dream.server.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteResponseDto {
    private Long voteItemId;
    private String subject;
    private Integer voteCount;
}