package com.example.chatapp.responses.message;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageItem implements Serializable {

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("sender")
    private String sender;

    @SerializedName("unread")
    private boolean unread;

    @SerializedName("__v")
    private int V;

    @SerializedName("_id")
    private String id;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("conversation")
    private String conversation;

    @SerializedName("content")
    private String content;

    @SerializedName("room")
    private String room;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public int getV() {
        return V;
    }

    public void setV(int v) {
        V = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}