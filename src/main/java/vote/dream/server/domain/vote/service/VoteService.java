package vote.dream.server.domain.vote.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vote.dream.server.domain.vote.dto.VoteItemDto;
import vote.dream.server.domain.vote.dto.VoteResponseDto;
import vote.dream.server.domain.vote.entity.Vote;
import vote.dream.server.domain.vote.entity.VoteItem;
import vote.dream.server.domain.vote.entity.VoteRecord;
import vote.dream.server.domain.vote.entity.VoteType;
import vote.dream.server.domain.vote.repository.VoteItemRepository;
import vote.dream.server.domain.vote.repository.VoteRecordRepository;
import vote.dream.server.domain.vote.repository.VoteRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Service
@Transactional
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private VoteItemRepository voteItemRepository;
    @Autowired
    private VoteRecordRepository voteRecordRepository;


    /*
    * 주어진 voteType에 해당하는 Vote 엔티티 조회
    * */
    public Optional<Vote> findByType(VoteType voteType) {
        return voteRepository.findByType(voteType);
    }

    // 1. 투표 항목 목록 조회 (GET) - 사용자가 투표하기 전에 어떤 항목이 있는지 보여줌
    public List<VoteItemDto> getVoteItems(VoteType voteType) {
        Vote vote = findByType(voteType)
                .orElseThrow(() -> new IllegalArgumentException("해당 타입의 투표가 없습니다."));
        return voteItemRepository.findByVoteId(vote.getVoteId())
                .stream()
                .map(item -> new VoteItemDto(item.getVoteItemId(), item.getSubject()))
                .collect(Collectors.toList());
    }

    // 2. 투표 결과 조회 (GET) - 각 투표 항목이 몇 표를 받았는지 결과를 보여줌
    public List<VoteResponseDto> getVoteResults(VoteType voteType) {
        Vote vote = findByType(voteType)
                .orElseThrow(() -> new IllegalArgumentException("해당 타입의 투표가 없습니다."));
        return voteItemRepository.findByVoteId(vote.getVoteId())
                .stream()
                .map(item -> new VoteResponseDto(item.getVoteItemId(), item.getSubject(), item.getVoteCount()))
                .collect(Collectors.toList());
    }

    // 3. 개인 투표 여부 확인 (GET, 인증 필요)
    public boolean hasVoted(Long userId, VoteType voteType) {
        Vote vote = findByType(voteType)
                .orElseThrow(() -> new IllegalArgumentException("해당 타입의 투표가 없습니다."));
        return voteRecordRepository.existsByUserIdAndVoteId(userId, vote.getVoteId());
    }

    // 4. 개인 투표 등록 (POST, 인증 필요)
    public void voteByType(Long userId, VoteType voteType, Long voteItemId) {

        // 1. voteType에 해당하는 Vote 엔티티 조회
        Vote vote = voteRepository.findByType(voteType)
                .orElseThrow(() -> new IllegalArgumentException("해당 타입의 투표가 없습니다."));

        // 2. 이미 투표했는지 확인
        if(voteRecordRepository.existsByUserIdAndVoteId(userId, vote.getVoteId())) {
            throw new IllegalArgumentException("이미 투표하셨습니다.");
        }
        // 3. 투표 기록 저장
        VoteRecord record = VoteRecord.builder()
                .userId(userId)
                .voteId(vote.getVoteId())
                .voteItemId(voteItemId)
                .build();
        voteRecordRepository.save(record);

        // 4. 투표 항목 득표수 증가
        VoteItem item = voteItemRepository.findById(voteItemId).orElseThrow();
        VoteItem updatedItem = item.incrementVoteCount();
        voteItemRepository.save(updatedItem);

    }
}

