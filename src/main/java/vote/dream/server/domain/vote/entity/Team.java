package vote.dream.server.domain.vote.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "team_name", nullable = false, unique = true)
    private String teamName;
}