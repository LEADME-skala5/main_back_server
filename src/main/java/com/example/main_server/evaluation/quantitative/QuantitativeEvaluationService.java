package com.example.main_server.evaluation.quantitative;

import com.example.main_server.common.entity.User;
import com.example.main_server.common.repository.UserRepository;
import com.example.main_server.evaluation.common.entity.Task;
import com.example.main_server.evaluation.common.entity.TaskParticipation;
import com.example.main_server.evaluation.common.entity.UserQuarterScore;
import com.example.main_server.evaluation.common.repository.TaskParticipationRepository;
import com.example.main_server.evaluation.common.repository.TaskRepository;
import com.example.main_server.evaluation.common.repository.UserQuarterScoreRepository;
import com.example.main_server.evaluation.quantitative.dto.QuarterOverviewResponse;
import com.example.main_server.evaluation.quantitative.dto.TaskEvaluation;
import com.example.main_server.evaluation.quantitative.dto.TaskResponse;
import com.example.main_server.evaluation.quantitative.dto.UserOverviewResponse;
import com.example.main_server.evaluation.quantitative.dto.WeeklyEvaluationRequest;
import com.example.main_server.evaluation.quantitative.entity.WeeklyEvaluation;
import com.example.main_server.evaluation.quantitative.repository.WeeklyEvaluationRepository;
import com.example.main_server.util.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuantitativeEvaluationService {
    private static final int YEAR = 2024;
    private static final int QUARTER = 1;

    private final WeeklyEvaluationRepository weeklyEvaluationRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskParticipationRepository taskParticipationRepository;
    private final UserQuarterScoreRepository userFinalScoreRepository;

    @Transactional
    public String saveEvaluation(WeeklyEvaluationRequest request) {
        User evaluator = userRepository.findById(request.evaluatorUserId())
                .orElseThrow(() -> new UserNotFoundException("평가자의 ID가 유효하지 않음"));

        User evaluatee = userRepository.findById(request.evaluateeUserId())
                .orElseThrow(() -> new UserNotFoundException("피평가자의 ID가 유효하지 않음"));

        List<Long> taskIds = request.evaluations().stream()
                .map(TaskEvaluation::taskId)
                .toList();

        Map<Long, Task> taskMap = taskRepository.findAllById(taskIds).stream()
                .collect(Collectors.toMap(Task::getId, Function.identity()));

        List<WeeklyEvaluation> evaluations = new ArrayList<>();

        for (TaskEvaluation eval : request.evaluations()) {
            Task task = taskMap.get(eval.taskId());
            if (task == null) {
                throw new IllegalArgumentException("Task ID가 유효하지 않음: " + eval.taskId());
            }

            WeeklyEvaluation entity = WeeklyEvaluation.builder()
                    .evaluatorUser(evaluator)
                    .evaluateeUser(evaluatee)
                    .task(task)
                    .grade(eval.grade())
                    .evaluationQuarter(QUARTER)
                    .evaluationYear(YEAR)
                    .build();

            evaluations.add(entity);
        }

        weeklyEvaluationRepository.saveAll(evaluations);

        return "저장 완료";
    }

    @Transactional
    public QuarterOverviewResponse getEvaluations(Long orgId, int year, int quarter) {
        // 1. 모든 필요한 데이터를 한 번에 조회
        EvaluationData evaluationData = fetchAllEvaluationData(orgId, year, quarter);

        // 2. 평가 완료 여부 확인
        boolean isEvaluationComplete = !evaluationData.hasUnevaluatedUsers();

        // 3. 사용자별 응답 생성
        List<UserOverviewResponse> userResponses = createUserResponses(evaluationData, isEvaluationComplete);

        return new QuarterOverviewResponse(isEvaluationComplete, userResponses);
    }

    private EvaluationData fetchAllEvaluationData(Long orgId, int year, int quarter) {
        // 한 번의 쿼리로 모든 사용자 조회 (fetch join으로 organization, job 포함)
        List<User> users = userRepository.findByOrganizationIdWithDetails(orgId);

        // 사용자 ID 목록 추출
        List<Long> userIds = users.stream().map(User::getId).toList();

        // 한 번의 쿼리로 모든 TaskParticipation 조회 (fetch join으로 task 포함)
        List<TaskParticipation> participations = taskParticipationRepository
                .findCurrentTaskParticipationsByUserIds(userIds);

        // 한 번의 쿼리로 모든 평가 데이터 조회
        List<WeeklyEvaluation> evaluations = weeklyEvaluationRepository
                .findByUserIdsAndPeriod(userIds, year, quarter);

        // 평가 완료된 사용자들의 점수 조회
        List<UserQuarterScore> scores = userFinalScoreRepository
                .findByOrgAndPeriod(orgId, year, quarter);

        return new EvaluationData(users, participations, evaluations, scores);
    }

    private List<UserOverviewResponse> createUserResponses(EvaluationData data, boolean isEvaluationComplete) {
        // 데이터를 Map에 저장
        Map<Long, List<TaskParticipation>> participationsByUser = data.participations().stream()
                .collect(Collectors.groupingBy(p -> p.getUser().getId()));

        Map<String, WeeklyEvaluation> evaluationMap = data.evaluations().stream()
                .collect(Collectors.toMap(
                        e -> e.getEvaluateeUser().getId() + "_" + e.getTask().getId(),
                        Function.identity()
                ));

        Map<Long, UserQuarterScore> scoreMap = data.scores().stream()
                .collect(Collectors.toMap(s -> s.getUser().getId(), Function.identity()));

        List<UserOverviewResponse> responses = data.users().stream()
                .map(user -> createUserResponse(user, participationsByUser, evaluationMap, scoreMap))
                .toList();

        // 평가 완료 시 점수순으로 정렬
        if (isEvaluationComplete) {
            responses = responses.stream()
                    .sorted(Comparator.comparing(UserOverviewResponse::quarterScore,
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .toList();
        }

        return responses;
    }

    private UserOverviewResponse createUserResponse(
            User user,
            Map<Long, List<TaskParticipation>> participationsByUser,
            Map<String, WeeklyEvaluation> evaluationMap,
            Map<Long, UserQuarterScore> scoreMap
    ) {
        List<TaskParticipation> userParticipations = participationsByUser.getOrDefault(user.getId(), List.of());

        List<TaskResponse> taskResponses = userParticipations.stream()
                .map(participation -> createTaskResponse(participation, evaluationMap, user.getId()))
                .toList();

        boolean isUserEvaluated = taskResponses.isEmpty() ||
                taskResponses.stream().allMatch(TaskResponse::isEvaluated);

        UserQuarterScore score = scoreMap.get(user.getId());

        return new UserOverviewResponse(
                user.getId(),
                user.getName(),
                user.getJob().getName(),
                user.getPrimaryEmail(),
                taskResponses,
                score != null ? score.getFinalScore() : null,
                score != null ? score.getUserRank() : null,
                score != null ? score.getUpdatedAt().toLocalDate() : null,
                isUserEvaluated
        );
    }

    private TaskResponse createTaskResponse(
            TaskParticipation participation,
            Map<String, WeeklyEvaluation> evaluationMap,
            Long userId
    ) {
        Task task = participation.getTask();
        String evaluationKey = userId + "_" + task.getId();
        WeeklyEvaluation evaluation = evaluationMap.get(evaluationKey);

        boolean isEvaluated = evaluation != null;
        Integer grade = isEvaluated ? evaluation.getGrade() : null;

        return new TaskResponse(
                task.getId(),
                task.getName(),
                participation.getStartDate(),
                participation.getEndDate(),
                isEvaluated,
                grade
        );
    }

    private record EvaluationData(List<User> users, List<TaskParticipation> participations,
                                  List<WeeklyEvaluation> evaluations, List<UserQuarterScore> scores) {
        public boolean hasUnevaluatedUsers() {
            return scores.size() < users.size();
        }
    }
}