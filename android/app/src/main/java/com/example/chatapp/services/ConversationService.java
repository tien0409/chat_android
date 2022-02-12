package com.example.chatapp.services;

import com.example.chatapp.responses.conversation.ConversationItem;
import com.example.chatapp.responses.conversation.ConversationResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ConversationService {
    @POST("conversation/{receiverId}")
    Call<ConversationItem> createConversation(@Path("receiverId") String receiverId);

    @GET("conversation")
    Call<ConversationResponse> getAllConversation();
}
