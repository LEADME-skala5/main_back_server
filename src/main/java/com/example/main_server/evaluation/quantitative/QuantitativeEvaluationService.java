package com.example.main_server.evaluation.quantitative;

import com.example.main_server.common.entity.User;
import com.example.main_server.common.repository.UserRepository;
import com.example.main_server.evaluation.common.entity.Task;
import com.example.main_server.evaluation.common.repository.TaskRepository;
import com.example.main_server.evaluation.quantitative.dto.TaskEvaluation;
import com.example.main_server.evaluation.quantitative.dto.WeeklyEvaluationRequest;
import com.example.main_server.evaluation.quantitative.entity.WeeklyEvaluation;
import com.example.main_server.evaluation.quantitative.repository.WeeklyEvaluationRepository;
import com.example.main_server.util.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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
}
