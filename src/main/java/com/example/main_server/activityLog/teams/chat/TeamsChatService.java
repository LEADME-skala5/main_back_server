package com.example.main_server.activityLog.teams.chat;

import com.example.main_server.activityLog.graphapi.GraphApiAccessTokenHandler;
import com.example.main_server.activityLog.teams.chat.entity.TeamsAttachment;
import com.example.main_server.activityLog.teams.chat.entity.TeamsMessage;
import com.example.main_server.activityLog.teams.chat.repository.TeamsMessageRepository;
import com.example.main_server.auth.user.UserRepository;
import com.example.main_server.auth.user.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Teams 채팅 관련 기능을 제공하는 서비스 클래스입니다.
 * <p>
 * Microsoft Graph API를 활용하여 Teams 채팅방 ID 조회, 채팅 메시지 데이터 가져오기 등의 기능을 제공합니다. 사용자의 Teams 채팅 데이터를 수집하고 저장하는 역할을 담당합니다.
 * <p>
 * 주요 기능: - 사용자의 채팅방 ID 목록 조회 - 특정 채팅방의 메시지 데이터 수집 및 저장 - 채팅 메시지의 첨부 파일 정보 처리
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamsChatService {
    private final RestClient graphApiRestClient;
    private final GraphApiAccessTokenHandler accessTokenHandler;
    private final UserRepository userRepo;
    private final TeamsMessageRepository teamsMessageRepository;

    public List<String> getChatRoomIds(String userId) {
        JsonNode response = graphApiRestClient.get()
                .uri("/users/{id}/chats", userId.trim())
                .headers(headers -> headers.setBearerAuth(accessTokenHandler.getAccessToken()))
                .retrieve()
                .body(JsonNode.class);

        return extractChatRoomIds(Objects.requireNonNull(response));
    }

    public void getRoomChatData(String chatRoomId) {
        JsonNode response = graphApiRestClient.get()
                .uri("/chats/{chatroom-id}/messages", chatRoomId)
                .headers(headers -> headers.setBearerAuth(accessTokenHandler.getAccessToken()))
                .retrieve()
                .body(JsonNode.class);

        if (response == null || !response.has("value")) {
            return;
        }

        for (JsonNode node : response.get("value")) {
            if (!node.has("from") || !node.path("from").has("user")) {
                continue;
            }
            JsonNode userNode = node.path("from").path("user");

            String teamsUserId = userNode.path("id").asText(null);
            if (teamsUserId == null) {
                continue;
            }

            Optional<User> optionalUser = userRepo.findByTeamsUserId(teamsUserId);
            if (optionalUser.isEmpty()) {
                log.warn("User not found for teamsUserId: {}", teamsUserId);
                continue;
            }

            User user = optionalUser.get();

            TeamsMessage teamsMessage = new TeamsMessage();
            teamsMessage.setChatId(node.path("chatId").asText());
            teamsMessage.setSender(user);
            teamsMessage.setContent(node.path("body").path("content").asText());
            teamsMessage.setCreatedAt(Instant.parse(node.path("createdDateTime").asText()));

            JsonNode attachments = node.path("attachments");
            if (attachments.isArray()) {
                for (JsonNode att : attachments) {
                    TeamsAttachment attachment = new TeamsAttachment();
                    attachment.setTeamsMessage(teamsMessage);
                    attachment.setAttachmentId(att.path("id").asText(null));
                    attachment.setName(att.path("name").asText(null));
                    attachment.setContentUrl(att.path("contentUrl").asText(null));
                    teamsMessage.getAttachments().add(attachment);
                }
            }

            teamsMessageRepository.save(teamsMessage);
        }
    }

    private List<String> extractChatRoomIds(JsonNode jsonNode) {
        List<String> ids = new ArrayList<>();
        JsonNode valueArray = jsonNode.get("value");
        if (valueArray != null && valueArray.isArray()) {
            for (JsonNode node : valueArray) {
                JsonNode idNode = node.get("id");
                if (idNode != null) {
                    ids.add(idNode.asText());
                }
            }
        }
        return ids;
    }
}
