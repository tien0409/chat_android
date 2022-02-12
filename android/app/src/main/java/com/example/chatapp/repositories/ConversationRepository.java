package com.example.chatapp.repositories;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.chatapp.responses.ErrorResponse;
import com.example.chatapp.responses.conversation.ConversationItem;
import com.example.chatapp.responses.conversation.ConversationResponse;
import com.example.chatapp.responses.message.MessageItem;
import com.example.chatapp.services.ConversationService;
import com.example.chatapp.services.RetrofitInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class ConversationRepository {
    private final MutableLiveData<ConversationItem> conversationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ConversationItem>> conversationsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MessageItem>> messagesMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<ErrorResponse> errorResponseMutableLiveData = new MutableLiveData<>();
    private final Application application;

    public ConversationRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<ConversationItem> getConversationMutableLiveData() {
        return conversationMutableLiveData;
    }

    public MutableLiveData<List<ConversationItem>> getAllConversationMutableLiveData() {
        return conversationsMutableLiveData;
    }
    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public MutableLiveData<ErrorResponse> getErrorResponseMutableLiveData() {
        return errorResponseMutableLiveData;
    }

    @NotNull
    private void getConversationsMutableLiveData(Call<ConversationResponse> call) {
        call.enqueue(new Callback<ConversationResponse>() {
            @Override
            public void onResponse(Call<ConversationResponse> call, Response<ConversationResponse> response) {
                if (response.isSuccessful()) {
                    ConversationResponse conversationResponse = response.body();
                    conversationsMutableLiveData.setValue(conversationResponse.getConversationItemList());
                } else {
                    try {
                        Gson gson = new GsonBuilder().create();
                        errorResponseMutableLiveData.setValue(gson.fromJson(response.errorBody().string(), ErrorResponse.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                loadingLiveData.setValue(false);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ConversationResponse> call, Throwable t) {
                Log.d("failed network conversation: ", t.getMessage());
            }
        });
    }

    @NotNull
    private void getConversationMutableLiveData(Call<ConversationItem> call) {
        call.enqueue(new Callback<ConversationItem>() {
            @Override
            public void onResponse(Call<ConversationItem> call, Response<ConversationItem> response) {
                if (response.isSuccessful()) {
                    ConversationItem conversationItem = response.body();
                    conversationMutableLiveData.setValue(conversationItem);
                } else {
                    try {
                        Gson gson = new GsonBuilder().create();
                        errorResponseMutableLiveData.setValue(gson.fromJson(response.errorBody().string(), ErrorResponse.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                loadingLiveData.setValue(false);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ConversationItem> call, Throwable t) {
                Log.d("failed network conversation: ", t.getMessage());
            }
        });
    }

    public void createConversation(String receiverId) {
        loadingLiveData.setValue(true);
        ConversationService conversationService = RetrofitInstance.getConversationService(application.getApplicationContext());
        Call<ConversationItem> call = conversationService.createConversation(receiverId);
        getConversationMutableLiveData(call);
    }

    public void getAllConversation() {
        loadingLiveData.setValue(true);
        ConversationService conversationService = RetrofitInstance.getConversationService(application.getApplicationContext());
        Call<ConversationResponse> call = conversationService.getAllConversation();
        getConversationsMutableLiveData(call);
    }
}
