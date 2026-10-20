package com.aidan.restaurant_assistant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private EmbeddingService embeddingService;
    
    @Autowired
    private DocumentRepository documentRepository;

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
    List<Double> embeddingList = embeddingService.getEmbedding(q);

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