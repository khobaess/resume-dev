package com.example.resumedev.service;

import com.example.resumedev.dto.UserDto;
import com.example.resumedev.exception.VkConnectionException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

@Service
@Slf4j
public class VkApiService {

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    @Value("${app.vk.service-token}")
    private String serviceToken;

    @Value("${app.vk.api-url}")
    private String VK_API_URL;

    @Value("${app.vk.api-version}")
    private String API_VERSION;

    public VkApiService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl(VK_API_URL)
                .build();
        this.objectMapper = objectMapper;
    }

    public UserDto getUserInfo(Long userId) {
        log.debug("Getting VK user info for user ID: {}", userId);

        try {
            JsonNode root = webClient.post()
                    .uri("users.get")
                    .body(BodyInserters.fromFormData("user_ids", userId.toString())
                            .with("fields", "first_name,last_name")
                            .with("access_token", serviceToken)
                            .with("v", API_VERSION))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (root == null) {
                throw new VkConnectionException("Empty response from VK API");
            }

            if (root.has("error")) {
                JsonNode error = root.get("error");
                String errorMsg = error.get("error_msg").asText();
                throw new VkConnectionException("VK API error: " + errorMsg);
            }

            JsonNode responseArray = root.get("response");
            if (responseArray == null || !responseArray.isArray() || responseArray.isEmpty()) {
                throw new VkConnectionException("User not found or empty response from VK");
            }

            JsonNode userNode = responseArray.get(0);

            return objectMapper.treeToValue(userNode, UserDto.class);

        } catch (WebClientResponseException e) {
            log.error("HTTP error calling VK API: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new VkConnectionException("VK API call failed", e);
        } catch (Exception e) {
            log.error("Error calling VK API", e);
            throw new VkConnectionException("VK API call failed", e);
        }
    }

    public boolean isValidVkUser(Long vkId) {
        try {
            getUserInfo(vkId);

            return true;
        } catch (Exception e) {
            log.warn("Failed to validate VK user: {}", vkId, e);

            return false;
        }
    }
}