package com.aidan.restaurant_assistant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EmbeddingService embeddingService;

    @Autowired
    private AnswerService answerService;

    @Autowired
private QuestionLogRepository questionLogRepository;
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @PostMapping
    public Document createDocument(@RequestBody Document document) {
        List<Double> embeddingList = embeddingService.getEmbedding(document.getContent());

        float[] embeddingArray = new float[embeddingList.size()];
        for (int i = 0; i < embeddingList.size(); i++) {
            embeddingArray[i] = embeddingList.get(i).floatValue();
        }

        document.setEmbedding(embeddingArray);

        return documentRepository.save(document);
    }

    @GetMapping("/search")
    public List<Document> search(@RequestParam String q,
                                 @RequestParam(defaultValue = "2") int limit) {
        return findRelevant(q, limit);
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String q) {
        List<Document> relevant = findRelevant(q, 2);
        String rawAnswer = answerService.answer(q, relevant);
    
        boolean answered = !rawAnswer.contains("NOT_COVERED");
        String answer = answered
                ? rawAnswer
                : "I don't have that information in our policies. Please ask a manager.";
    
        String topTitle = answered && !relevant.isEmpty() ? relevant.get(0).getTitle() : null;
        questionLogRepository.save(new QuestionLog(q, answer, topTitle, answered));
    
        return answer;
    }

    private List<Document> findRelevant(String question, int limit) {
        List<Double> embeddingList = embeddingService.getEmbedding(question);

        StringBuilder vectorText = new StringBuilder("[");
        for (int i = 0; i < embeddingList.size(); i++) {
            if (i > 0) {
                vectorText.append(",");
            }
            vectorText.append(embeddingList.get(i));
        }
        vectorText.append("]");

        return documentRepository.findMostSimilar(vectorText.toString(), limit);
    }
}