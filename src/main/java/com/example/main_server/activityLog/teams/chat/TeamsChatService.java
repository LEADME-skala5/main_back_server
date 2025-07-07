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
