package com.sk.skala.skore.evaluation.peer.dto;

import com.sk.skala.skore.evaluation.common.dto.TaskInfoResponse;
import java.util.List;

public record PeerInfoResponse(Long userId, String name, List<TaskInfoResponse> task) {
}

