package com.aidan.restaurant_assistant;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class EmbeddingService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    public List<Double> getEmbedding(String text) {
        Map<String, Object> requestBody = Map.of(
                "model", "text-embedding-3-small",
                "input", text
        );

        Map<String, Object> response = restClient.post()
                .uri("https://api.openai.com/v1/embeddings")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
        Map<String, Object> firstResult = data.get(0);
        return (List<Double>) firstResult.get("embedding");
    }
}
