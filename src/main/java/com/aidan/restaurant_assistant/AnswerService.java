package com.aidan.restaurant_assistant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class AnswerService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    public String answer(String question, List<Document> documents) {
        StringBuilder context = new StringBuilder();
        for (Document doc : documents) {
            context.append("Title: ").append(doc.getTitle()).append("\n");
            context.append(doc.getContent()).append("\n\n");
        }

        String systemPrompt = "You are an assistant for restaurant staff. Answer the question using ONLY "
        + "the documents provided. If the documents do not contain the answer, reply with exactly "
        + "NOT_COVERED and nothing else. Otherwise keep answers short and practical.";
        String userPrompt = "Documents:\n" + context + "Question: " + question;

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        Map<String, Object> response = restClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }
}