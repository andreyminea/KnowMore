package com.example.knlgsharing;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Comment
{
    String name;
    String question;
    ArrayList<Answer> answerArrayList;
    DatabaseReference ref;

    public Comment() {
    }

    public Comment(String title, String question, ArrayList<Answer> answers, DatabaseReference ref) {
        name = title;
        this.question = question;
        answerArrayList = answers;
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        name = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Answer> getAnswerArrayList() {
        return answerArrayList;
    }

    public void setAnswerArrayList(ArrayList<Answer> answerArrayList) {
        this.answerArrayList = answerArrayList;
    }
}
