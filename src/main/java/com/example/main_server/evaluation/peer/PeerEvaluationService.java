package com.example.main_server.evaluation.peer;

import com.example.main_server.auth.user.entity.User;
import com.example.main_server.common.repository.UserRepository;
import com.example.main_server.evaluation.common.dto.TaskInfoResponse;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PeerEvaluationService {
    private static final int MAX_CONTRIBUTION_SCORE = 100;
    private static final int MIN_CONTRIBUTION_SCORE = 1;
    private static final int YEAR = 2025;
    private static final int QUARTER = 2;
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
        Map<Long, List<TaskInfoResponse>> peerTaskMap = new HashMap<>();

        for (TaskParticipation participation : userParticipation) {
            Long taskId = participation.getTask().getId();

            // 3. 같은 task에 참여한 다른 사용자들 조회 (자기 자신 제외)
            List<TaskParticipation> otherParticipants =
                    taskParticipationRepository.findByTaskIdAndUserIdNot(taskId, userId);

            // 4. 각 동료별로 공통 참여 Task 정보 수집
            for (TaskParticipation otherParticipation : otherParticipants) {
                Long peerId = otherParticipation.getUser().getId();

                // 해당 동료와 공통으로 참여한 모든 Task 조회
                List<Task> commonTasks = taskParticipationRepository.findCommonTasksByUserIds(userId, peerId);

                List<TaskInfoResponse> taskInfoList = commonTasks.stream()
                        .map(task -> new TaskInfoResponse(task.getId(), task.getName()))
                        .toList();

                peerTaskMap.put(peerId, taskInfoList);
            }
        }

        // 5. 중복 제거된 동료 목록 생성
        Set<Long> processedPeerIds = new HashSet<>();
        List<PeerInfoResponse> peers = new ArrayList<>();

        for (TaskParticipation participation : userParticipation) {
            Long taskId = participation.getTask().getId();
            List<TaskParticipation> otherParticipants =
                    taskParticipationRepository.findByTaskIdAndUserIdNot(taskId, userId);

            for (TaskParticipation otherParticipation : otherParticipants) {
                Long peerId = otherParticipation.getUser().getId();

                if (!processedPeerIds.contains(peerId)) {
                    peers.add(new PeerInfoResponse(
                            peerId,
                            otherParticipation.getUser().getName(),
                            peerTaskMap.get(peerId)
                    ));
                    processedPeerIds.add(peerId);
                }
            }
        }

        return peers;
    }


    public List<EvaluationKeyword> getKeywords() {
        return evaluationKeywordRepository.findAll();
    }

    // TODO: 동료 평가 여부 파악 추가해야함
    @Transactional
    public PeerKeywordEvaluationResponse savePeerKeywords(PeerKeywordEvaluationRequest request) {

        User evaluator = userRepository.findById(request.evaluatorUserId())
                .orElseThrow(() -> new UserNotFoundException("평가자를 찾을 수 없습니다. ID: " + request.evaluatorUserId()));

        // 1. 필요한 모든 ID를 미리 추출
        List<Long> allEvaluateeIds = request.evaluations().stream()
                .map(PeerKeywordEvaluationRequest.Evaluation::evaluateeUserId)
                .toList();

        List<Long> allKeywordIds = request.evaluations().stream()
                .flatMap(eval -> eval.keywordIds().stream())
                .distinct()
                .toList();

        Map<Long, User> evaluateeMap = userRepository.findAllById(allEvaluateeIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        Map<Long, EvaluationKeyword> keywordMap = evaluationKeywordRepository.findAllById(allKeywordIds).stream()
                .collect(Collectors.toMap(EvaluationKeyword::getId, keyword -> keyword));

        List<PeerKeywordEvaluation> evaluations = new ArrayList<>();

        for (PeerKeywordEvaluationRequest.Evaluation eval : request.evaluations()) {
            if (request.evaluatorUserId().equals(eval.evaluateeUserId())) {
                throw new InvalidEvaluationRequestException("자기 자신을 평가할 수 없습니다.");
            }

            User evaluatee = evaluateeMap.get(eval.evaluateeUserId());
            if (evaluatee == null) {
                throw new UserNotFoundException("피평가자를 찾을 수 없습니다. ID: " + eval.evaluateeUserId());
            }

            if (eval.keywordIds() == null || eval.keywordIds().isEmpty()) {
                throw new InvalidEvaluationRequestException("키워드 ID 목록이 비어있습니다. 피평가자 ID: " + eval.evaluateeUserId());
            }

            for (Long keywordId : eval.keywordIds()) {
                EvaluationKeyword keyword = keywordMap.get(keywordId);
                if (keyword == null) {
                    throw new KeywordNotFoundException("키워드를 찾을 수 없습니다. ID: " + keywordId);
                }

                PeerKeywordEvaluation evaluation = new PeerKeywordEvaluation();
                evaluation.setEvaluator(evaluator);
                evaluation.setEvaluatee(evaluatee);
                evaluation.setKeyword(keyword);
                evaluation.setEvaluationYear(YEAR);
                evaluation.setEvaluationQuarter(QUARTER);

                evaluations.add(evaluation);
            }
        }

        List<PeerKeywordEvaluation> savedEvaluations = peerKeywordEvaluationRepository.saveAll(evaluations);

        return new PeerKeywordEvaluationResponse("동료 평가가 성공적으로 저장되었습니다.", savedEvaluations.size());
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