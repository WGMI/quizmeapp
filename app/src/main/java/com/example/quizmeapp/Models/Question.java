package com.example.quizmeapp.Models;

public class Question {

    String question;
    String[] optionArray = new String[4];
    int answer;

    public Question(){

    }

    public Question(String question, String[] optionArray, int answer) {
        this.question = question;
        this.optionArray = optionArray;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptionArray() {
        return optionArray;
    }

    public void setOptionArray(String[] optionArray) {
        this.optionArray = optionArray;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
