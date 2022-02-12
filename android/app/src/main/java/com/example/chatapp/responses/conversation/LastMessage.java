package com.example.chatapp.responses.conversation;

import com.google.gson.annotations.SerializedName;

public class LastMessage{

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

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSender(String sender){
		this.sender = sender;
	}

	public String getSender(){
		return sender;
	}

	public void setUnread(boolean unread){
		this.unread = unread;
	}

	public boolean isUnread(){
		return unread;
	}

	public void setV(int V){
		this.V = V;
	}

	public int getV(){
		return V;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return avatar;
	}

	public void setConversation(String conversation){
		this.conversation = conversation;
	}

	public String getConversation(){
		return conversation;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}
}