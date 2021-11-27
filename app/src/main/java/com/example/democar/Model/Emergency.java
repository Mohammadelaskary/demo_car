package com.example.democar.Model;

import java.util.ArrayList;
import java.util.List;

public class Emergency {
    private String question;
    private String steps;
    private ArrayList<Integer> images;

    public Emergency() {
    }

    public Emergency(String question, String steps, ArrayList<Integer> images) {
        this.question = question;
        this.steps = steps;
        this.images = images;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setImages(ArrayList<Integer> images) {
        this.images = images;
    }
}
