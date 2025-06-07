package com.example.main_server.evaluation.peer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/peer")
@RequiredArgsConstructor
public class PeerEvaluationController {
    private final PeerEvaluationService peerEvaluationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PeerResponseDTO>> getPeers(@PathVariable Long userId) {
        List<PeerResponseDTO> peers = peerEvaluationService.getPeers(userId);
        return ResponseEntity.ok(peers);
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<String>> getPeerKeywords() {
        peerEvaluationService.getKeywords();
        return ResponseEntity.ok(keywords);
    }

    @PostMapping("/evaluation")
    public ResponseEntity<Void> savePeerKeywords(@RequestBody PeerKeywordRequestDTO request) {
        peerEvaluationService.savePeerKeywords(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/contribution")
    public ResponseEntity<Void> saveContributionScore(@RequestBody ContributionRequestDTO request) {
        peerEvaluationService.saveContributionScore(request);
        return ResponseEntity.ok().build();
    }
}
