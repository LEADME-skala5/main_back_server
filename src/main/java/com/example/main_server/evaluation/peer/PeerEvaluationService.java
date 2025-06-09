package com.example.main_server.evaluation.peer;

import com.example.main_server.common.entity.User;
import com.example.main_server.common.repository.UserRepository;
import com.example.main_server.evaluation.common.entity.Task;
import com.example.main_server.evaluation.common.entity.TaskParticipation;
import com.example.main_server.evaluation.common.repository.TaskParticipationRepository;
import com.example.main_server.evaluation.common.repository.TaskRepository;
import com.example.main_server.evaluation.peer.dto.PeerInfoResponse;
import com.example.main_server.evaluation.peer.dto.PeerKeywordEvaluationRequest;
import com.example.main_server.evaluation.peer.dto.PeerKeywordEvaluationResponse;
import com.example.main_server.evaluation.peer.dto.PeerTaskContributionEvaluationRequest;
import com.example.main_server.evaluation.peer.entity.EvaluationKeyword;
import com.example.main_server.evaluation.peer.entity.PeerKeywordEvaluation;
import com.example.main_server.evaluation.peer.entity.PeerTaskContributionEvaluation;
import com.example.main_server.evaluation.peer.exception.InvalidEvaluationRequestException;
import com.example.main_server.evaluation.peer.exception.KeywordNotFoundException;
import com.example.main_server.evaluation.peer.repository.EvaluationKeywordRepository;
import com.example.main_server.evaluation.peer.repository.PeerKeywordEvaluationRepository;
import com.example.main_server.evaluation.peer.repository.PeerTaskContributionEvaluationRepository;
import com.example.main_server.util.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PeerEvaluationService {
    private static final int MAX_CONTRIBUTION_SCORE = 100;
    private static final int MIN_CONTRIBUTION_SCORE = 1;
    private final EvaluationKeywordRepository evaluationKeywordRepository;
    private final TaskParticipationRepository taskParticipationRepository;
    private final PeerKeywordEvaluationRepository peerKeywordEvaluationRepository;
    private final UserRepository userRepository;
    private final PeerTaskContributionEvaluationRepository peerTaskContributionEvaluationRepository;
    private final TaskRepository taskRepository;

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

    public List<EvaluationKeyword> getKeywords() {
        return evaluationKeywordRepository.findAll();
    }

    @Transactional
    public PeerKeywordEvaluationResponse savePeerKeywords(PeerKeywordEvaluationRequest request) {
        // 입력 값 검증
        validateRequest(request);

        // 사용자 조회
        User evaluator = userRepository.findById(request.evaluatorUserId())
                .orElseThrow(() -> new UserNotFoundException("평가자를 찾을 수 없습니다. ID: " + request.evaluatorUserId()));

        User evaluatee = userRepository.findById(request.evaluateeUserId())
                .orElseThrow(() -> new UserNotFoundException("피평가자를 찾을 수 없습니다. ID: " + request.evaluateeUserId()));

        List<PeerKeywordEvaluation> evaluations = new ArrayList<>();

        // 각 키워드별로 평가 엔티티 생성
        for (Long keywordId : request.keywordIds()) {
            EvaluationKeyword keyword = evaluationKeywordRepository.findById(keywordId)
                    .orElseThrow(() -> new KeywordNotFoundException("키워드를 찾을 수 없습니다. ID: " + keywordId));

            PeerKeywordEvaluation evaluation = new PeerKeywordEvaluation();
            evaluation.setEvaluator(evaluator);
            evaluation.setEvaluatee(evaluatee);
            evaluation.setKeyword(keyword);

            evaluations.add(evaluation);
        }

        // 일괄 저장
        List<PeerKeywordEvaluation> savedEvaluations = peerKeywordEvaluationRepository.saveAll(evaluations);

        return new PeerKeywordEvaluationResponse("동료 평가가 성공적으로 저장되었습니다.", savedEvaluations.size());
    }

    private void validateRequest(PeerKeywordEvaluationRequest request) {
        if (request.keywordIds() == null || request.keywordIds().isEmpty()) {
            throw new InvalidEvaluationRequestException("키워드 ID 목록이 비어있습니다.");
        }

        if (request.evaluatorUserId().equals(request.evaluateeUserId())) {
            throw new InvalidEvaluationRequestException("자기 자신을 평가할 수 없습니다.");
        }

        if (request.evaluateeUserId() == null) {
            throw new InvalidEvaluationRequestException("평가자와 피평가자 ID는 필수입니다.");
        }
    }

    @Transactional
    public void saveContributionScore(PeerTaskContributionEvaluationRequest request) {
        // 입력 값 검증
        validateContributionRequest(request);

        // 엔티티 조회
        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new RuntimeException("태스크를 찾을 수 없습니다. ID: " + request.taskId()));

        User evaluator = userRepository.findById(request.evaluatorUserId())
                .orElseThrow(() -> new UserNotFoundException("평가자를 찾을 수 없습니다. ID: " + request.evaluatorUserId()));

        User target = userRepository.findById(request.targetUserId())
                .orElseThrow(() -> new UserNotFoundException("피평가자를 찾을 수 없습니다. ID: " + request.targetUserId()));

        // 기여도 평가 엔티티 생성
        PeerTaskContributionEvaluation evaluation = new PeerTaskContributionEvaluation();
        evaluation.setTask(task);
        evaluation.setEvaluator(evaluator);
        evaluation.setTarget(target);
        evaluation.setScore(request.score());

        // 저장
        peerTaskContributionEvaluationRepository.save(evaluation);
    }

    private void validateContributionRequest(PeerTaskContributionEvaluationRequest request) {
        if (request.taskId() == null) {
            throw new InvalidEvaluationRequestException("태스크 ID는 필수입니다.");
        }

        if (request.evaluatorUserId() == null) {
            throw new InvalidEvaluationRequestException("평가자 ID는 필수입니다.");
        }

        if (request.targetUserId() == null) {
            throw new InvalidEvaluationRequestException("피평가자 ID는 필수입니다.");
        }

        if (request.score() == null) {
            throw new InvalidEvaluationRequestException("점수는 필수입니다.");
        }

        if (request.evaluatorUserId().equals(request.targetUserId())) {
            throw new InvalidEvaluationRequestException("자기 자신을 평가할 수 없습니다.");
        }

        if (request.score() < MIN_CONTRIBUTION_SCORE || request.score() > MAX_CONTRIBUTION_SCORE) {
            throw new InvalidEvaluationRequestException("유효하지 않은 기여도입니다.");
        }
    }
}