package com.example.knlgsharing;

public class Answer
{
    String name;
    String response;

    public Answer() {
    }

    public Answer(String name, String response) {
        this.name = name;
        response = response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String answer) {
        this.response = answer;
    }
}
