package vote.dream.server.domain.vote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vote.dream.server.domain.vote.dto.VoteItemDto;
import vote.dream.server.domain.vote.dto.VoteRequestDto;
import vote.dream.server.domain.vote.dto.VoteResponseDto;
import vote.dream.server.domain.vote.entity.VoteType;
import vote.dream.server.domain.vote.service.VoteService;
import vote.dream.server.domain.user.entity.User;
import java.util.List;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;

    // 1. 투표 항목 목록 조회 (비로그인 허용)
    @GetMapping("/{voteId}/items")
    public List<VoteItemDto> getVoteItems(@PathVariable Long voteId) {
        return voteService.getVoteItems(voteId);
    }

    // 2. 투표 결과 조회 (비로그인 허용)
    @GetMapping("/{voteId}/results")
    public List<VoteResponseDto> getVoteResults(@PathVariable Long voteId) {
        return voteService.getVoteResults(voteId);
    }

    // 3. 내 투표 여부 확인 (로그인 필요)
    @GetMapping("/{voteType}/status")
    public Map<String, Boolean> hasVoted(@PathVariable Long voteId, @AuthenticationPrincipal User user) {
        boolean voted = voteService.hasVoted(user.getId(), voteId);
        return Collections.singletonMap("voted", voted);
    }

    // 4. 투표하기 (로그인 필요)
    @PostMapping("/{voteType}/vote")
    public ResponseEntity<?> voteByType(@PathVariable VoteType voteType,
                                  @RequestBody VoteRequestDto dto,
                                  @AuthenticationPrincipal User user) {
        voteService.voteByType(user.getId(), voteType, dto.getVoteItemId());
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }
}
