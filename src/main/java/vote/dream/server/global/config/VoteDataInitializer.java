package vote.dream.server.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vote.dream.server.domain.vote.entity.Vote;
import vote.dream.server.domain.vote.entity.VoteStatus;
import vote.dream.server.domain.vote.entity.VoteType;
import vote.dream.server.domain.vote.repository.VoteItemRepository;
import vote.dream.server.domain.vote.repository.VoteRepository;
import vote.dream.server.domain.vote.entity.VoteItem;
import vote.dream.server.domain.vote.entity.Team;
import vote.dream.server.domain.vote.repository.TeamRepository;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class VoteDataInitializer implements CommandLineRunner {
    private final TeamRepository teamRepo;
    private final VoteRepository voteRepo;
    private final VoteItemRepository itemRepo;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 1) 팀 세팅
        List<String> teamNames = List.of("POPUPCYCLE", "HONEYHOME", "DEARDREAM", "INFLUEE", "PROMETHA");
        for (String name : teamNames) {
            teamRepo.findByTeamName(name)
                    .orElseGet(() -> teamRepo.save(new Team(null, name)));
        }

        // 2) 투표 종류 세팅
        for (VoteType type : VoteType.values()) {
            voteRepo.findByType(type)
                    .orElseGet(() -> voteRepo.save(new Vote(null, type, VoteStatus.WAITING)));
        }


        // 3) 파트장 투표용 후보 세팅
        Vote plVote = voteRepo.findByType(VoteType.PARTLEADER).get();
        Long plVoteId = plVote.getVoteId();
        List<VoteItem> existingPlItems = itemRepo.findByVoteId(plVoteId);

        // 팀별 후보 매핑
        Map<String, List<String>> plCandidatesByTeam = Map.of(
                "POPUPCYCLE", List.of("박준형", "임도현", "김철흥", "송아영"),
                "HONEYHOME",   List.of("이석원", "최근호", "신수진", "원채영"),
                "DEARDREAM",   List.of("오지현", "한혜수", "김영서", "이주희"),
                "PROMETHA",    List.of("박정하", "서채연", "권동욱", "김서연"),
                "INFLUEE",     List.of("박채연", "박서연", "최서연", "한서정")
        );

        for (var entry : plCandidatesByTeam.entrySet()) {
            String teamName = entry.getKey();
            Long teamId = teamRepo.findByTeamName(teamName)
                    .orElseThrow()
                    .getTeamId();

            for (String candidate : entry.getValue()) {
                boolean exists = existingPlItems.stream()
                        .anyMatch(i ->
                                i.getSubject().equals(candidate) &&
                                        i.getTeamId().equals(teamId)
                        );
                if (!exists) {
                    itemRepo.save(new VoteItem(
                            null,        // voteItemId (auto-generated)
                            candidate,   // subject
                            0,           // voteCount
                            plVoteId,    // voteId
                            teamId       // teamId
                    ));
                }
            }
        }


        // 4) 데모데이 투표용 후보 세팅 (팀 단위)
        Vote ddVote = voteRepo.findByType(VoteType.DEMODAY).get();
        Long ddVoteId = ddVote.getVoteId();
        for (Team t : teamRepo.findAll(Sort.by(Sort.Direction.ASC, "teamId"))) {
            String teamName = t.getTeamName();
            Long teamId = t.getTeamId();

            boolean exists = itemRepo.findByVoteId(ddVoteId).stream()
                    .anyMatch(i ->
                            i.getSubject().equals(teamName) &&
                                    i.getTeamId().equals(teamId)
                    );
            if (!exists) {
                itemRepo.save(new VoteItem(
                        null,          // voteItemId (auto-generated)
                        teamName,      // subject
                        0,             // voteCount
                        ddVoteId,      // voteId(Long)
                        teamId         // teamId(Long)
                ));
            }


        }
    }
}