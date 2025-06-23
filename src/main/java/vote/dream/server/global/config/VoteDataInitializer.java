package vote.dream.server.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
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
import java.util.stream.Collectors;

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
        List<Long> existingPlItemIds = itemRepo.findByVoteId(plVote.getVoteId())
                .stream()
                .map(VoteItem::getVoteItemId)
                .collect(Collectors.toList());

        // (a) 백엔드 파트장 후보
        Team backend = teamRepo.findByTeamName("DEARDREAM").get();
        List<String> backendNames = List.of(
                "박준형", "서채연", "오지현", "박서연", "한혜수",
                "박채연", "박정하", "이석원", "임도현", "최근호"
        );
        Long plVoteId = plVote.getVoteId();
        Long backendTeamId = backend.getTeamId();
        List<VoteItem> existingPlItems = itemRepo.findByVoteId(plVoteId);

        for (String nm : backendNames) {
            boolean exists = existingPlItems.stream()
                    .anyMatch(i ->
                            i.getSubject().equals(nm)
                                    && i.getTeamId().equals(backendTeamId)      // ★ getTeamId() 사용
                    );
            if (!exists) {
                itemRepo.save(new VoteItem(
                        null,
                        nm,
                        0,
                        plVoteId,           // voteId만
                        backendTeamId       // teamId만
                ));
            }
        }

        // (b) 프론트엔드 파트장 후보
        Team frontend = teamRepo.findByTeamName("POPUPCYCLE").get();
        List<String> frontendNames = List.of("김서연", "신수진", "김영서", "원채영",
                "김철홍", "이주희", "권동욱", "최서연",
                "송아영", "한서정");
        Long frontendTeamId = frontend.getTeamId();
        for (String nm : frontendNames) {
            boolean exists = existingPlItems.stream()
                    .anyMatch(i ->
                            i.getSubject().equals(nm)
                                    && i.getTeamId().equals(frontendTeamId)    // ★ getTeamId() 사용
                    );
            if (!exists) {
                itemRepo.save(new VoteItem(
                        null,
                        nm,
                        0,
                        plVoteId,
                        frontendTeamId
                ));
            }
        }


        // 4) 데모데이 투표용 후보 세팅 (팀 단위)
        Vote ddVote = voteRepo.findByType(VoteType.DEMODAY).get();
        Long ddVoteId = ddVote.getVoteId();
        for (Team t : teamRepo.findAll()) {
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