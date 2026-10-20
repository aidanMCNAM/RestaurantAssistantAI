package com.aidan.restaurant_assistant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query(value = "SELECT * FROM document ORDER BY embedding <=> CAST(:queryEmbedding AS vector) LIMIT :limit",
           nativeQuery = true)
    List<Document> findMostSimilar(@Param("queryEmbedding") String queryEmbedding,
                                   @Param("limit") int limit);
}