package com.example.main_server.evaluation.peer;

import com.example.main_server.evaluation.peer.dto.PeerInfoResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PeerEvaluationService {
    private final PeerEvaluationRepository peerEvaluationRepository;

    public List<PeerInfoResponse> getPeers(Long userId) {
    }
}
