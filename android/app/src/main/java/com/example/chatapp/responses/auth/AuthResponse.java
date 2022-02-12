package com.example.chatapp.responses.auth;

import com.google.gson.annotations.SerializedName;

public class AuthResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}