package com.example.main_server.evaluation.peer.dto;

import com.example.main_server.evaluation.common.dto.TaskInfoResponse;
import java.util.List;

public record PeerInfoResponse(Long userId, String name, List<TaskInfoResponse> task) {
}

