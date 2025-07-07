package com.example.main_server.evaluation.quantitative;

import com.example.main_server.auth.user.UserRepository;
import com.example.main_server.auth.user.entity.User;
import com.example.main_server.auth.user.exception.UserNotFoundException;
import com.example.main_server.evaluation.common.entity.Task;
import com.example.main_server.evaluation.common.entity.TaskParticipation;
import com.example.main_server.evaluation.common.entity.UserQuarterScore;
import com.example.main_server.evaluation.common.repository.TaskParticipationRepository;
import com.example.main_server.evaluation.common.repository.TaskRepository;
import com.example.main_server.evaluation.common.repository.UserQuarterScoreRepository;
import com.example.main_server.evaluation.quantitative.dto.QuarterEvaluationSummary;
import com.example.main_server.evaluation.quantitative.dto.QuarterEvaluationSummaryImpl.QuarterEvaluationCompletedSummary;
import com.example.main_server.evaluation.quantitative.dto.QuarterEvaluationSummaryImpl.QuarterEvaluationInProgressSummary;
import com.example.main_server.evaluation.quantitative.dto.QuarterOverviewResponse;
import com.example.main_server.evaluation.quantitative.dto.TaskEvaluation;
import com.example.main_server.evaluation.quantitative.dto.TaskResponse;
import com.example.main_server.evaluation.quantitative.dto.UserOverviewResponse;
import com.example.main_server.evaluation.quantitative.dto.WeeklyEvaluationRequest;
import com.example.main_server.evaluation.quantitative.entity.WeeklyEvaluation;
import com.example.main_server.evaluation.quantitative.repository.WeeklyEvaluationRepository;
import com.example.main_server.util.EvaluationPeriodService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * 정성적 평가 기능을 제공하는 서비스 클래스입니다.
 * <p>
 * 조직별 정성적 평가 키워드를 관리하고, AI를 활용한 키워드 생성 기능을 제공합니다. 조직의 특성에 맞는 정성적 평가 기준을 설정하고 관리합니다.
 * <p>
 * 주요 기능: - 조직별 평가 키워드 저장 - AI 기반 평가 키워드 생성 - 외부 AI 모델 서버와의 연동
 */


@Service
@RequiredArgsConstructor
public class QuantitativeEvaluationService {
    private final EvaluationPeriodService evaluationPeriodService;
    private final WeeklyEvaluationRepository weeklyEvaluationRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskParticipationRepository taskParticipationRepository;
    private final UserQuarterScoreRepository userFinalScoreRepository;
    private final RestClient aiServerRestClient;
    private final MongoTemplate mongoTemplate;

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
                    .evaluationQuarter(evaluationPeriodService.getCurrentQuarter())
                    .evaluationYear(evaluationPeriodService.getCurrentYear())
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
        List<UserOverviewResponse> userResponses = createUserResponses(evaluationData, isEvaluationComplete, year,
                quarter);

        QuarterEvaluationSummary summary = isEvaluationComplete
                ? createCompletedSummary(evaluationData)
                : createInProgressSummary(evaluationData);

        return new QuarterOverviewResponse(isEvaluationComplete, year, quarter, userResponses, summary);
    }

    private QuarterEvaluationInProgressSummary createInProgressSummary(EvaluationData evaluationData) {
        int totalTargetUsers = evaluationData.users().size();
        int evaluatedUserCount = evaluationData.scores().size();
        double progress = totalTargetUsers == 0 ? 0 : (evaluatedUserCount * 100.0) / totalTargetUsers;
        int ongoingProjects = countOngoingProjects(evaluationData.participations());

        return new QuarterEvaluationInProgressSummary(totalTargetUsers, evaluatedUserCount, progress, ongoingProjects);
    }

    private QuarterEvaluationCompletedSummary createCompletedSummary(EvaluationData evaluationData) {
        int totalUsers = evaluationData.users().size();
        double averageScore = calculateAverageScore(evaluationData.scores());
        int ongoingProjects = countOngoingProjects(evaluationData.participations());

        return new QuarterEvaluationCompletedSummary(totalUsers, averageScore, ongoingProjects);
    }

    private EvaluationData fetchAllEvaluationData(Long orgId, int year, int quarter) {
        // 한 번의 쿼리로 모든 사용자 조회
        List<User> users = userRepository.findByOrganizationIdWithDetails(orgId);

        // 사용자 ID 목록 추출
        List<Long> userIds = users.stream().map(User::getId).toList();

        // 한 번의 쿼리로 모든 TaskParticipation 조회
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

    private List<UserOverviewResponse> createUserResponses(EvaluationData data, boolean isEvaluationComplete, int year,
                                                           int quarter) {
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
                .map(user -> createUserResponse(user, participationsByUser, evaluationMap, scoreMap, year, quarter))
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
            Map<Long, UserQuarterScore> scoreMap,
            int year,
            int quarter
    ) {
        List<TaskParticipation> userParticipations = participationsByUser.getOrDefault(user.getId(), List.of());

        List<TaskResponse> taskResponses = userParticipations.stream()
                .map(participation -> createTaskResponse(participation, evaluationMap, user.getId()))
                .toList();

        boolean isUserEvaluated = taskResponses.isEmpty() ||
                taskResponses.stream().allMatch(TaskResponse::isEvaluated);

        UserQuarterScore score = scoreMap.get(user.getId());

        // 몽고 DB에서 year 와 quarter에 맞는 reportId 가져오기
        String reportId = findQuarterReportId(user.getId(), year, quarter);

        return new UserOverviewResponse(
                user.getId(),
                user.getName(),
                user.getJob().getName(),
                user.getPrimaryEmail(),
                reportId,
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

    private int countOngoingProjects(List<TaskParticipation> participations) {
        return (int) participations.stream()
                .map(p -> p.getTask().getId())
                .distinct()
                .count();
    }

    private double calculateAverageScore(List<UserQuarterScore> scores) {
        return scores.stream()
                .map(UserQuarterScore::getFinalScore)
                .mapToDouble(BigDecimal::doubleValue) // BigDecimal → double
                .average()
                .orElse(0.0);
    }

    private String findQuarterReportId(Long userId, int year, int quarter) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is("personal-quarter"))
                .addCriteria(Criteria.where("evaluated_year").is(year))
                .addCriteria(Criteria.where("evaluated_quarter").is(quarter))
                .addCriteria(Criteria.where("user.userId").is(userId));

        org.bson.Document result = mongoTemplate.findOne(query, org.bson.Document.class, "reports");
        return result != null ? result.getObjectId("_id").toHexString() : null;
    }

    public String generateEvaluation(WeeklyEvaluationRequest request) {
        User evaluatee = userRepository.findById(request.evaluateeUserId())
                .orElseThrow(() -> new UserNotFoundException("피평가자의 ID가 유효하지 않음"));

        int currentYear = evaluationPeriodService.getCurrentYear();
        int currentQuarter = evaluationPeriodService.getCurrentQuarter();

        boolean reportExists = checkReportExists(evaluatee.getId(), currentYear, currentQuarter);
        if (reportExists) {
            return "이미 해당 분기에 평가 보고서가 존재합니다.";
        }

        try {
            String response = aiServerRestClient.post()
                    .uri("/weekly-report/batch-evaluate")
                    .body(request)
                    .retrieve()
                    .body(String.class);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("AI 서버 요청 실패: " + e.getMessage(), e);
        }
    }

    private boolean checkReportExists(Long userId, int year, int quarter) {
        Criteria criteria = Criteria.where("user.userId").is(userId)
                .and("type").is("personal-quarter")
                .and("evaluatedYear").is(year)
                .and("evaluatedQuarter").is(quarter);

        Query query = new Query(criteria);
        return mongoTemplate.exists(query, "reports");
    }

    public Object getWeeklyAISummary(Long userId, int year, int quarter) {
        String userKey = String.valueOf(userId);
        String quarterKey = year + "Q" + quarter;

        String basePath = String.format("users.%s.quarters.%s", userKey, quarterKey);
        String teamGoalsPath = basePath + ".teamGoals";

        Query query = new Query();

        query.addCriteria(Criteria.where("data_type").is("personal-quarter"));

        query.addCriteria(Criteria.where("users." + userKey).exists(true));

        query.addCriteria(Criteria.where(basePath + ".evaluated_year").is(year));
        query.addCriteria(Criteria.where(basePath + ".evaluated_quarter").is(quarter));

        query.fields().include(teamGoalsPath);

        Document result = mongoTemplate.findOne(query, Document.class, "weekly_evaluation_results");
        if (result == null) {
            return null;
        }

        Document users = (Document) result.get("users");
        if (users == null || !users.containsKey(userKey)) {
            return null;
        }

        Document userDoc = (Document) users.get(userKey);
        Document quarters = (Document) userDoc.get("quarters");
        if (quarters == null || !quarters.containsKey(quarterKey)) {
            return null;
        }

        Document quarterData = (Document) quarters.get(quarterKey);
        List<Map<String, Object>> teamGoals = (List<Map<String, Object>>) quarterData.get("teamGoals");

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        Map<String, Object> response = new HashMap<>();
        response.put("user", Map.of(
                "userId", user.getId(),
                "name", user.getName(),
                "department", user.getDepartment().getName(),
                "job", user.getJob().getName()
        ));
        response.put("teamGoals", teamGoals);

        return response;
    }

    private record EvaluationData(List<User> users, List<TaskParticipation> participations,
                                  List<WeeklyEvaluation> evaluations, List<UserQuarterScore> scores) {
        public boolean hasUnevaluatedUsers() {
            return scores.size() < users.size();
        }
    }
}