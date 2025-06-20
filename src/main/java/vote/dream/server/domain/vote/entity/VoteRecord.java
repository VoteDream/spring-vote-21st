package vote.dream.server.domain.vote.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "vote_record")
public class VoteRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_record_id")
    private Long voteRecordId;

    @NotNull
    private Long userId;

    @NotNull
    private Long voteId;

    @NotNull
    private Long voteItemId;
}
