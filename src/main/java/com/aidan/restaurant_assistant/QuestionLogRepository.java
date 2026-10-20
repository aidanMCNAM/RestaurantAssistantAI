package com.aidan.restaurant_assistant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionLogRepository extends JpaRepository<QuestionLog, Long> {

    long countByAnswered(boolean answered);

    List<QuestionLog> findTop20ByOrderByAskedAtDesc();

    List<QuestionLog> findByAnsweredFalseOrderByAskedAtDesc();

    @Query("SELECT q.topDocumentTitle, COUNT(q) FROM QuestionLog q "
            + "WHERE q.topDocumentTitle IS NOT NULL "
            + "GROUP BY q.topDocumentTitle ORDER BY COUNT(q) DESC")
    List<Object[]> findDocumentUsage();
}