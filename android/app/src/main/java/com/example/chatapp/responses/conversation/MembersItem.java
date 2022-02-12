package com.example.chatapp.responses.conversation;

import com.google.gson.annotations.SerializedName;

public class MembersItem{

	@SerializedName("_id")
	private String id;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("username")
	private String username;

	public MembersItem(String id) {
		this.id = id;
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

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}
}