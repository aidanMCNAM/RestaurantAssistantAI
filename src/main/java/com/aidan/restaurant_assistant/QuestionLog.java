package com.aidan.restaurant_assistant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class QuestionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String question;

    @Column(length = 4000)
    private String answer;

    private String topDocumentTitle;

    private boolean answered;

    private Instant askedAt;

    public QuestionLog() {
    }

    public QuestionLog(String question, String answer, String topDocumentTitle, boolean answered) {
        this.question = question;
        this.answer = answer;
        this.topDocumentTitle = topDocumentTitle;
        this.answered = answered;
        this.askedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getTopDocumentTitle() {
        return topDocumentTitle;
    }

    public boolean isAnswered() {
        return answered;
    }

    public Instant getAskedAt() {
        return askedAt;
    }
}