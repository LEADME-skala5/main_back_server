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
import com.example.main_server.evaluation.quantitative.dto.UserOverview;
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
    private static final int YEAR = 2021;
    private static final int QUARTER = 2;

    private final WeeklyEvaluationRepository weeklyEvaluationRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskParticipationRepository taskParticipationRepository;
    private final UserQuarterScoreRepository userFinalScoreRepository;

    @Transactional
    public void saveEvaluation(WeeklyEvaluationRequest request) {
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
    }

    @Transactional
    public QuarterOverviewResponse getEvaluations(Long orgId, int year, int quarter) {
        // 해당 조직에 모든 유저가 평가 받았는지 확인
        boolean hasUnevaluated = weeklyEvaluationRepository.existsUnEvaluatedUsers(orgId, year, quarter);

        // 평가가 완료가 안되었을 경우
        if (hasUnevaluated) {
            List<User> members = userRepository.findByOrganizationId(orgId);

            List<UserOverview> dtos = members.stream()
                    .map(u -> {
                        // 직접 TaskParticipation 목록을 조회
                        List<TaskParticipation> participations = taskParticipationRepository.findCurrentTaskParticipations(
                                u.getId());

                        List<TaskResponse> taskResponses = participations.stream()
                                .map(participation -> {
                                    Task task = participation.getTask();
                                    return new TaskResponse(
                                            task.getId(),
                                            task.getName(),
                                            participation.getStartDate(),
                                            participation.getEndDate(),
                                            task.getWeight()
                                    );
                                })
                                .toList();

                        return new UserOverview(
                                u.getId(),
                                u.getName(),
                                u.getJob().getName(),
                                u.getPrimaryEmail(),
                                taskResponses,
                                null,       // score 미정
                                null,       // rank 미정
                                null        // lastUpdated 미정
                        );
                    })
                    .toList();

            return new QuarterOverviewResponse(false, dtos);
        }

        // 평가가 모두 이루어졌을 경우
        List<UserQuarterScore> scores = userFinalScoreRepository.findByOrgAndPeriod(orgId, year, quarter);

        List<UserOverview> dtos = scores.stream()
                .sorted(Comparator.comparing(UserQuarterScore::getFinalScore).reversed())
                .map(s -> {
                    User u = s.getUser();
                    // 직접 TaskParticipation 목록을 조회
                    List<TaskParticipation> participations = taskParticipationRepository.findCurrentTaskParticipations(
                            u.getId());

                    List<TaskResponse> taskResponses = participations.stream()
                            .map(participation -> {
                                Task task = participation.getTask();
                                return new TaskResponse(
                                        task.getId(),
                                        task.getName(),
                                        participation.getStartDate(),
                                        participation.getEndDate(),
                                        task.getWeight()
                                );
                            })
                            .toList();

                    return new UserOverview(
                            u.getId(),
                            u.getName(),
                            u.getJob().getName(),
                            u.getPrimaryEmail(),
                            taskResponses,
                            s.getFinalScore(),
                            s.getUserRank(),
                            s.getUpdatedAt().toLocalDate()
                    );
                })
                .toList();

        return new QuarterOverviewResponse(true, dtos);
    }

}
