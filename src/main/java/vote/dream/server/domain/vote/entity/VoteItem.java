package vote.dream.server.domain.vote.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "vote_item")
public class VoteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_item_id")
    private Long voteItemId;

    @NotNull
    private String subject;

    private Integer voteCount;

    @NotNull
    private Long voteId;

    @NotNull
    private Long teamId;

    public VoteItem incrementVoteCount() {
        return VoteItem.builder()
                .voteItemId(this.voteItemId)
                .subject(this.subject)
                .voteCount(this.voteCount + 1)
                .voteId(this.voteId)
                .teamId(this.teamId)
                .build();
    }
}
