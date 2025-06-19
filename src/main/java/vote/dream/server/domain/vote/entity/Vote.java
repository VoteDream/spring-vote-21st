package vote.dream.server.domain.vote.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`vote`")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VoteType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VoteStatus status;
}
