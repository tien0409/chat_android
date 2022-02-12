package com.example.chatapp.responses.conversation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConversationItem {

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("members")
    private List<MembersItem> members;

    @SerializedName("__v")
    private int V;

    @SerializedName("_id")
    private String id;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("lastMessage")
    private LastMessage lastMessage;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<MembersItem> getMembers() {
        return members;
    }

    public void setMembers(List<MembersItem> members) {
        this.members = members;
    }

    public int getV() {
        return V;
    }

    public void setV(int V) {
        this.V = V;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
}