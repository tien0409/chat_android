package com.example.chatapp.responses.conversation;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ConversationResponse{

	@SerializedName("data")
	private List<ConversationItem> conversationItemList;

	@SerializedName("message")
	private String message;

	public void setData(List<ConversationItem> data){
		this.conversationItemList = data;
	}

	public List<ConversationItem> getConversationItemList(){
		return conversationItemList;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}