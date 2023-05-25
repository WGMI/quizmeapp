package com.example.quizme.Models;

import com.example.quizmeapp.DB.AppDB;
import com.reactiveandroid.Model;
import com.reactiveandroid.annotation.Column;
import com.reactiveandroid.annotation.PrimaryKey;
import com.reactiveandroid.annotation.Table;

@Table(name = "Article", database = AppDB.class)
public class Article extends Model {

    @PrimaryKey
    long id;
    @Column
    String text;
    @Column
    String questions;

    public Article(){

    }
    public Article(long id, String text, String questions) {
        this.id = id;
        this.text = text;
        this.questions = questions;
    }

    public Article(String text, String questions) {
        this.text = text;
        this.questions = questions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
}
