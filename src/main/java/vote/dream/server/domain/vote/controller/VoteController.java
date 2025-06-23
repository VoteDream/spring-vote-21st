package vote.dream.server.domain.vote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vote.dream.server.domain.jwt.Custom.CustomDetails;
import vote.dream.server.domain.vote.dto.VoteItemDto;
import vote.dream.server.domain.vote.dto.VoteRequestDto;
import vote.dream.server.domain.vote.dto.VoteResponseDto;
import vote.dream.server.domain.vote.entity.Vote;
import vote.dream.server.domain.vote.entity.VoteType;
import vote.dream.server.domain.vote.service.VoteService;
import vote.dream.server.domain.user.entity.User;
import vote.dream.server.global.apiPayload.ApiResponse;
import vote.dream.server.global.apiPayload.status.SuccessStatus;

import java.util.List;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;

    // 1. 투표 항목 목록 조회 (비로그인 허용)
    @GetMapping("/{voteType}/items")
    public ApiResponse<List<VoteItemDto>> getVoteItems(@PathVariable VoteType voteType) {
        return ApiResponse.onSuccess(voteService.getVoteItems(voteType));
    }

    // 2. 투표 결과 조회 (비로그인 허용)
    @GetMapping("/{voteType}/results")
    public ApiResponse<List<VoteResponseDto>> getVoteResults(@PathVariable VoteType voteType) {
        return ApiResponse.onSuccess(voteService.getVoteResults(voteType));
    }

    // 3. 내 투표 여부 확인 (로그인 필요)
    @GetMapping("/{voteType}/status")
    public ApiResponse<Map<String, Boolean>> hasVoted(@PathVariable("voteType") VoteType voteType, @AuthenticationPrincipal CustomDetails user) {
        Vote vote = voteService.findByType(voteType)
                .orElseThrow(() -> new IllegalArgumentException("해당 타입의 투표가 없습니다."));
        boolean voted = voteService.hasVoted(user.getUser().getId(), voteType);
        return ApiResponse.onSuccess(
                Collections.singletonMap("voted", voted)
        );
    }

    // 4. 투표하기 (로그인 필요)
    @PostMapping("/{voteType}/vote")
    public ApiResponse<?> voteByType(@PathVariable VoteType voteType,
                                  @RequestBody VoteRequestDto dto,
                                  @AuthenticationPrincipal CustomDetails user) {
        voteService.voteByType(user.getUser().getId(), voteType, dto.getVoteItemId());
        return ApiResponse.onSuccess(null);
    }

    // 테스트용 동적 메서드
    @GetMapping("/{voteType}/ping")
    public ResponseEntity<String> ping(@PathVariable VoteType voteType) {
        return ResponseEntity.ok("got " + voteType);
    }

}
