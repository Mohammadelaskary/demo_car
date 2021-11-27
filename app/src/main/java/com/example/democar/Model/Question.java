package com.example.democar.Model;

import java.util.List;

public class Question {
    private String question;
    private String answer;
    private List<Integer> images;

    public Question(String question, String answer, List<Integer> images) {
        this.question = question;
        this.answer = answer;
        this.images = images;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<Integer> getImages() {
        return images;
    }

    public void setImages(List<Integer> images) {
        this.images = images;
    }
}
