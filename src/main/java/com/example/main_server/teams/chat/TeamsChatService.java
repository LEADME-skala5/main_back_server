package com.example.main_server.teams.chat;

import com.example.main_server.auth.graphapi.GraphApiAccessTokenHandler;
import com.example.main_server.common.User;
import com.example.main_server.common.UserRepository;
import com.example.main_server.teams.chat.entity.TeamsAttachment;
import com.example.main_server.teams.chat.entity.TeamsMessage;
import com.example.main_server.teams.chat.repository.TeamsMessageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TeamsChatService {
    private final RestClient graphApiRestClient;
    private final GraphApiAccessTokenHandler accessTokenHandler;
    private final UserRepository userRepo;
    private final TeamsMessageRepository messageRepo;

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

        if (response == null) {
            return;
        }

        JsonNode root = response.get("value");
        if (root == null || !root.isArray()) {
            return;
        }

        for (JsonNode node : root) {
            String teamsUserId = node.path("from").path("user").path("id").asText(null);
            if (teamsUserId == null) {
                continue;
            }

            Optional<User> optionalUser = userRepo.findByTeamsUserId(teamsUserId);
            if (optionalUser.isEmpty()) {
                System.out.println("User not found for teamsUserId: " + teamsUserId);
                continue;
            }

            User user = optionalUser.get();

            TeamsMessage msg = new TeamsMessage();
            msg.setUser(user);
            msg.setTeamsUserId(teamsUserId);
            msg.setTeamsMessageId(node.path("id").asText());
            msg.setTeamsDisplayName(node.path("from").path("user").path("displayName").asText(null));
            msg.setMessageType(node.path("messageType").asText(null));
            msg.setBodyContentType(node.path("body").path("contentType").asText(null));
            msg.setBodyContent(node.path("body").path("content").asText(null));

            Instant created = Instant.parse(node.path("createdDateTime").asText());
            msg.setCreatedDatetime(Timestamp.from(created));
            msg.setLogDate(LocalDateTime.ofInstant(created, ZoneOffset.UTC).toLocalDate());
            msg.setMessageDatetime(Timestamp.from(created));

            JsonNode attachments = node.path("attachments");
            if (attachments.isArray()) {
                for (JsonNode att : attachments) {
                    TeamsAttachment ta = new TeamsAttachment();
                    ta.setMessage(msg);
                    ta.setUser(user);
                    ta.setTeamsUserId(teamsUserId);
                    ta.setTeamsMessageId(msg.getTeamsMessageId());
                    ta.setTeamsAttachmentsId(att.path("id").asText(null));
                    ta.setAttachmentName(att.path("name").asText(null));
                    ta.setContentUrl(att.path("contentUrl").asText(null));
                    ta.setLogDate(msg.getLogDate());
                    msg.getAttachments().add(ta);
                }
            }

            messageRepo.save(msg);
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
