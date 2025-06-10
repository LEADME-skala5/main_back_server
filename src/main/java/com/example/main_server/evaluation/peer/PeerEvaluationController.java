package com.example.main_server.evaluation.peer;

import com.example.main_server.evaluation.peer.dto.EvaluationKeywordResponse;
import com.example.main_server.evaluation.peer.dto.PeerInfoResponse;
import com.example.main_server.evaluation.peer.dto.PeerKeywordEvaluationRequest;
import com.example.main_server.evaluation.peer.dto.PeerKeywordEvaluationResponse;
import com.example.main_server.evaluation.peer.dto.PeerTaskContributionEvaluationRequest;
import com.example.main_server.evaluation.peer.entity.EvaluationKeyword;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/peer-evaluation")
@RequiredArgsConstructor
public class PeerEvaluationController {
    private final PeerEvaluationService peerEvaluationService;

    @GetMapping("/peers/{userId}")
    public ResponseEntity<List<PeerInfoResponse>> getPeers(@PathVariable Long userId) {
        List<PeerInfoResponse> peers = peerEvaluationService.getPeers(userId);
        return ResponseEntity.ok(peers);
    }

    @GetMapping("/keywords")
    public List<EvaluationKeywordResponse> getPeerKeywords() {
        List<EvaluationKeyword> keywords = peerEvaluationService.getKeywords();
        return keywords.stream()
                .map(k -> new EvaluationKeywordResponse(k.getId(), k.getKeyword(), k.getIsPositive()))
                .toList();
    }

    @PostMapping("/keyword-evaluation")
    public ResponseEntity<PeerKeywordEvaluationResponse> savePeerKeywords(
            @RequestBody PeerKeywordEvaluationRequest request) {
        PeerKeywordEvaluationResponse response = peerEvaluationService.savePeerKeywords(request);
        URI location = URI.create("/keyword-evaluation");
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/contribution-evaluation")
    public ResponseEntity<Void> saveContributionScore(@RequestBody PeerTaskContributionEvaluationRequest request) {
        peerEvaluationService.saveContributionScore(request);
        URI location = URI.create("/contribution-evaluation");
        return ResponseEntity.created(location).build();
    }
}
