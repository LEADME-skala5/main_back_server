package com.example.main_server.activityLog.teams.teams.chat;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/chat")
public class TeamsChatController {

    private final TeamsChatService teamsChatService;

    @GetMapping("/{user-id}")
    public ResponseEntity<String> saveUserChatHistory(@PathVariable("user-id") String userId) {
        List<String> chatRoomIds = teamsChatService.getChatRoomIds(userId);
        for (String id : chatRoomIds) {
            teamsChatService.getRoomChatData(id);
        }

        return ResponseEntity.ok(userId + "의 채팅 내역 저장 성공");
    }
}
