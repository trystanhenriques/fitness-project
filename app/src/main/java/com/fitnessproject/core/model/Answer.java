package com.fitnessproject.core.model;

public class Answer {
    private final String questionId;
    private final String choiceId;

    public Answer(String questionId, String choiceId) {
        this.questionId = questionId;
        this.choiceId = choiceId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getChoiceId() {
        return choiceId;
    }
}
