package com.example.chatapp.responses.auth;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("_id")
	private String id;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("email")
	private String email;

	@SerializedName("username")
	private String username;

	@SerializedName("token")
	private String token;

	@SerializedName("password")
	private String password;

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
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

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}