package vote.dream.server.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteRequestDto {
    private Long voteId;
    private Long voteItemId;
}