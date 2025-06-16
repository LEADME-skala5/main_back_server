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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuantitativeEvaluationService {
    private final WeeklyEvaluationRepository weeklyEvaluationRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public void saveEvaluation(WeeklyEvaluationRequest request) {
        User evaluator = userRepository.findById(request.evaluatorUserId())
                .orElseThrow(() -> new UserNotFoundException("평가자의 ID가 유효하지 않음"));

        User evaluatee = userRepository.findById(request.evaluateeUserId())
                .orElseThrow(() -> new UserNotFoundException("피평가자의 ID가 유효하지 않음"));

        for (TaskEvaluation eval : request.evaluations()) {
            Task task = taskRepository.findById(eval.taskId())
                    .orElseThrow(() -> new IllegalArgumentException("Task ID가 유효하지 않음"));

            WeeklyEvaluation entity = WeeklyEvaluation.builder()
                    .evaluatorUser(evaluator)
                    .evaluateeUser(evaluatee)
                    .task(task)
                    .grade(eval.grade())
                    .build();

            weeklyEvaluationRepository.save(entity);
        }
    }
}
