package com.sk.skala.skore.evaluation.peer;

import com.sk.skala.skore.evaluation.common.dto.TaskInfoResponse;
import com.sk.skala.skore.evaluation.common.entity.Task;
import com.sk.skala.skore.evaluation.common.entity.TaskParticipation;
import com.sk.skala.skore.evaluation.common.repository.TaskParticipationRepository;
import com.sk.skala.skore.evaluation.common.repository.TaskRepository;
import com.sk.skala.skore.evaluation.peer.dto.PeerInfoResponse;
import com.sk.skala.skore.evaluation.peer.dto.PeerKeywordEvaluationRequest;
import com.sk.skala.skore.evaluation.peer.dto.PeerKeywordEvaluationResponse;
import com.sk.skala.skore.evaluation.peer.dto.PeerTaskContributionEvaluationRequest;
import com.sk.skala.skore.evaluation.peer.entity.EvaluationKeyword;
import com.sk.skala.skore.evaluation.peer.entity.PeerKeywordEvaluation;
import com.sk.skala.skore.evaluation.peer.entity.PeerTaskContributionEvaluation;
import com.sk.skala.skore.evaluation.peer.exception.InvalidEvaluationRequestException;
import com.sk.skala.skore.evaluation.peer.exception.KeywordNotFoundException;
import com.sk.skala.skore.evaluation.peer.repository.EvaluationKeywordRepository;
import com.sk.skala.skore.evaluation.peer.repository.PeerKeywordEvaluationRepository;
import com.sk.skala.skore.evaluation.peer.repository.PeerTaskContributionEvaluationRepository;
import com.sk.skala.skore.user.UserRepository;
import com.sk.skala.skore.user.entity.User;
import com.sk.skala.skore.user.exception.UserException;
import com.sk.skala.skore.user.exception.UserExceptionType;
import com.sk.skala.skore.util.EvaluationPeriodService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 동료 평가 기능을 제공하는 서비스 클래스입니다.
 * <p>
 * 사용자의 동료 목록 조회, 동료 평가 키워드 관리, 키워드 기반 평가 저장, 태스크 기여도 평가 등의 기능을 제공합니다. 동료 간 평가를 통해 다양한 관점에서의 피드백을 수집하고 관리합니다.
 * <p>
 * 주요 기능: - 평가 가능한 동료 목록 조회 (캐싱 적용) - 평가 키워드 목록 조회 - 키워드 기반 동료 평가 데이터 저장 - 태스크별 기여도 평가 저장 및 검증
 */


@Service
@RequiredArgsConstructor
public class PeerEvaluationService {
    private static final int MAX_CONTRIBUTION_SCORE = 100;
    private static final int MIN_CONTRIBUTION_SCORE = 1;
    private final EvaluationPeriodService evaluationPeriodService;
    private final EvaluationKeywordRepository evaluationKeywordRepository;
    private final TaskParticipationRepository taskParticipationRepository;
    private final PeerKeywordEvaluationRepository peerKeywordEvaluationRepository;
    private final UserRepository userRepository;
    private final PeerTaskContributionEvaluationRepository peerTaskContributionEvaluationRepository;
    private final TaskRepository taskRepository;

    @Cacheable(value = "peers", key = "#userId")
    public List<PeerInfoResponse> getPeers(Long userId) {
        // 1. 한 번의 쿼리로 모든 필요한 데이터를 조회 (fetch join 사용)
        List<TaskParticipation> userParticipations = taskParticipationRepository
                .findByUserIdWithTaskAndUser(userId);

        if (userParticipations.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 사용자가 참여한 모든 task ID 수집
        Set<Long> taskIds = userParticipations.stream()
                .map(tp -> tp.getTask().getId())
                .collect(Collectors.toSet());

        // 3. 한 번의 쿼리로 모든 관련 TaskParticipation 조회 (fetch join 사용)
        List<TaskParticipation> allParticipations = taskParticipationRepository
                .findByTaskIdsWithTaskAndUser(taskIds);

        // 4. 자기 자신 제외한 동료들만 필터링
        List<TaskParticipation> peerParticipations = allParticipations.stream()
                .filter(tp -> !tp.getUser().getId().equals(userId))
                .toList();

        // 5. 동료별로 공통 참여 Task 그룹화
        Map<Long, List<TaskInfoResponse>> peerTaskMap = peerParticipations.stream()
                .collect(Collectors.groupingBy(
                        tp -> tp.getUser().getId(),
                        Collectors.mapping(
                                tp -> new TaskInfoResponse(tp.getTask().getId(), tp.getTask().getName()),
                                Collectors.toList()
                        )
                ));

        // 6. 중복 제거된 동료 목록 생성
        Map<Long, PeerInfoResponse> peerMap = new HashMap<>();

        for (TaskParticipation peerParticipation : peerParticipations) {
            Long peerId = peerParticipation.getUser().getId();

            if (!peerMap.containsKey(peerId)) {
                peerMap.put(peerId, new PeerInfoResponse(
                        peerId,
                        peerParticipation.getUser().getName(),
                        peerTaskMap.get(peerId)
                ));
            }
        }

        return new ArrayList<>(peerMap.values());
    }


    public List<EvaluationKeyword> getKeywords() {
        return evaluationKeywordRepository.findAll();
    }

    // TODO: 동료 평가 여부 파악 추가해야함
    @Transactional
    public PeerKeywordEvaluationResponse savePeerKeywords(PeerKeywordEvaluationRequest request) {

        User evaluator = userRepository.findById(request.evaluatorUserId())
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

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
                throw new UserException(UserExceptionType.USER_NOT_FOUND);
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
                evaluation.setEvaluationYear(evaluationPeriodService.getCurrentYear());
                evaluation.setEvaluationQuarter(evaluationPeriodService.getCurrentQuarter());

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
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        User target = userRepository.findById(request.targetUserId())
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

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