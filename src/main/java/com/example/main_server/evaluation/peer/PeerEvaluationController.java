package com.example.main_server.evaluation.peer;

import com.example.main_server.evaluation.peer.dto.PeerInfoResponse;
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

    @GetMapping("/{userId}")
    public ResponseEntity<List<PeerInfoResponse>> getPeers(@PathVariable Long userId) {
        List<PeerInfoResponse> peers = peerEvaluationService.getPeers(userId);
        return ResponseEntity.ok(peers);
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<String>> getPeerKeywords() {
        peerEvaluationService.getKeywords();
        return ResponseEntity.ok(keywords);
    }

    @PostMapping("/evaluation")
    public ResponseEntity<Void> savePeerKeywords(@RequestBody PeerKeywordRequest request) {
        peerEvaluationService.savePeerKeywords(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/contribution")
    public ResponseEntity<Void> saveContributionScore(@RequestBody ContributionRequest request) {
        peerEvaluationService.saveContributionScore(request);
        return ResponseEntity.ok().build();
    }
}
