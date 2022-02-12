package com.example.chatapp.responses;

import com.example.chatapp.responses.user.DataItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersResponse {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("message")
    private String message;

    public List<DataItem> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
