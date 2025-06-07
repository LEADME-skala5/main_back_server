package com.example.main_server.evaluation.peer;

import com.example.main_server.evaluation.common.entity.TaskParticipation;
import com.example.main_server.evaluation.common.repository.TaskParticipationRepository;
import com.example.main_server.evaluation.peer.dto.PeerInfoResponse;
import com.example.main_server.evaluation.peer.repository.EvaluationKeywordRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PeerEvaluationService {
    private final EvaluationKeywordRepository evaluationKeywordRepository;
    private final TaskParticipationRepository taskParticipationRepository;

    public List<PeerInfoResponse> getPeers(Long userId) {
        // 1. userId로 해당 사용자가 참여한 모든 TaskParticipation 조회
        List<TaskParticipation> userParticipation = taskParticipationRepository.findByUserId(userId);

        // 2. 각 task에 대해 다른 참여자들 조회
        Set<PeerInfoResponse> peers = new HashSet<>();

        for (TaskParticipation participation : userParticipation) {
            Long taskId = participation.getTask().getId();

            // 3. 같은 task에 참여한 다른 사용자들 조회 (자기 자신 제외)
            List<TaskParticipation> otherParticipants =
                    taskParticipationRepository.findByTaskIdAndUserIdNot(taskId, userId);

            // 4. PeerInfoResponse로 변환하여 Set에 추가 (중복 제거)
            for (TaskParticipation otherParticipation : otherParticipants) {
                peers.add(new PeerInfoResponse(
                        otherParticipation.getUser().getId(),
                        otherParticipation.getUser().getName()
                ));
            }
        }

        return new ArrayList<>(peers);
    }

//    public void getKeywords() {
//        // TODO: 모든 키워드 가져와서 반환하기
//        evaluationKeywordRepository.findAll();
//    }

//    public void savePeerKeywords(PeerKeywordRequest request) {
//    }

//    public void saveContributionScore(ContributionRequest request) {
//    }
}