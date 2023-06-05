package com.example.quizmeapp.Models;

import com.example.quizmeapp.DB.AppDB;
import com.reactiveandroid.Model;
import com.reactiveandroid.annotation.Column;
import com.reactiveandroid.annotation.PrimaryKey;
import com.reactiveandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;

@Table(name = "Quiz", database = AppDB.class)
public class Quiz extends Model {

    @PrimaryKey
    long id;
    @Column
    String name;
    @Column
    String article;
    @Column
    String date;
    @Column
    String description;
    @Column
    String questionJSON;
    @Column
    int score;

    public Quiz(){

    }

    public Quiz(long id, String name, String article, String date, String description, String questionJSON) {
        this.id = id;
        this.name = name;
        this.article = article;
        this.date = date;
        this.description = description;
        this.questionJSON = questionJSON;
    }

    public Quiz(String name, String article, String date, String description, String questionJSON) {
        this.name = name;
        this.article = article;
        this.date = date;
        this.description = description;
        this.questionJSON = questionJSON;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestionJSON() {
        return questionJSON;
    }

    public void setQuestionJSON(String questionJSON) {
        this.questionJSON = questionJSON;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getQuestionCount() {
        int count = 1;
        try {
            JSONArray array = new JSONArray(this.getQuestionJSON());
            count = array.length();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
}
