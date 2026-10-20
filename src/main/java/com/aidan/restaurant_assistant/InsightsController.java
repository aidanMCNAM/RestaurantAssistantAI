package com.aidan.restaurant_assistant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/insights")
public class InsightsController {

    @Autowired
    private QuestionLogRepository questionLogRepository;

    @GetMapping
    public Map<String, Object> getInsights() {
        long answered = questionLogRepository.countByAnswered(true);
        long unanswered = questionLogRepository.countByAnswered(false);

        List<Map<String, Object>> documentUsage = new ArrayList<>();
        for (Object[] row : questionLogRepository.findDocumentUsage()) {
            documentUsage.add(Map.of("title", row[0], "count", row[1]));
        }

        Map<String, Object> insights = new LinkedHashMap<>();
        insights.put("totalQuestions", answered + unanswered);
        insights.put("answered", answered);
        insights.put("unanswered", unanswered);
        insights.put("documentUsage", documentUsage);
        insights.put("gaps", questionLogRepository.findByAnsweredFalseOrderByAskedAtDesc());
        insights.put("recent", questionLogRepository.findTop20ByOrderByAskedAtDesc());
        return insights;
    }
}