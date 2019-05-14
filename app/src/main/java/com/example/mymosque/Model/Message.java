package com.example.mymosque.Model;

import com.google.gson.annotations.SerializedName;

public class Message {

    public Message(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;



}
