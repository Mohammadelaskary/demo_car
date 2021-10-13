package com.example.democar.Model;

public class Emergency {
    private String question;
    private String body;

    public Emergency() {
    }

    public Emergency(String question, String body) {
        this.question = question;
        this.body = body;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
