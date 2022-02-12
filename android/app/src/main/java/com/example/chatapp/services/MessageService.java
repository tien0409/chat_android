package com.example.chatapp.services;

import com.example.chatapp.responses.message.MessageItem;
import com.example.chatapp.responses.message.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {
    @GET("message/{conversationId}")
    Call<MessageResponse> getMessages(@Path("conversationId") String conversationId);

    @POST("message/{conversationId}")
    Call<MessageResponse> sendMessage(@Path("conversationId") String conversationId, @Body MessageItem message);
}
