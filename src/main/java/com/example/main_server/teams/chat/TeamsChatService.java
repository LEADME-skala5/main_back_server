package com.example.main_server.teams.chat;

import com.example.main_server.auth.GraphApiAccessTokenHandler;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TeamsChatService {
    private final WebClient webClient;
    private final GraphApiAccessTokenHandler accessTokenHandler;

    public TeamsChatService(WebClient.Builder webClientBuilder, GraphApiAccessTokenHandler accessTokenHandler) {
        this.webClient = webClientBuilder.baseUrl("https://graph.microsoft.com/v1.0").build();
        this.accessTokenHandler = accessTokenHandler;
    }

    public List<String> getChatRoomIds(String userId) {
        JsonNode response = webClient.get()
                .uri("/users/{id}/chats", userId.trim())
                .headers(headers -> headers.setBearerAuth(accessTokenHandler.getAccessToken()))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return extractChatRoomIds(response);
    }

    public String getRoomChatData(String chatRoomId) {
        JsonNode response = webClient.get()
                .uri("/chats/{chatroom-id}/messages", chatRoomId)
                .headers(headers -> headers.setBearerAuth(accessTokenHandler.getAccessToken()))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return response.toString();
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
