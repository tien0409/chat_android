package com.example.chatapp.responses.message;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageResponse {

    @SerializedName("data")
    private List<MessageItem> messageItems;

    @SerializedName("message")
    private String message;

    public List<MessageItem> getMessageItems() {
        return messageItems;
    }

    public String getMessage() {
        return message;
    }
}