package com.example.chatapp.repositories;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.chatapp.responses.ErrorResponse;
import com.example.chatapp.responses.message.MessageItem;
import com.example.chatapp.responses.message.MessageResponse;
import com.example.chatapp.services.MessageService;
import com.example.chatapp.services.RetrofitInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class MessageRepository {
    private final MutableLiveData<List<MessageItem>> messagesMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<ErrorResponse> errorResponseMutableLiveData = new MutableLiveData<>();
    private final Application application;

    public MessageRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<MessageItem>> getMessagesMutableLiveData() {
        return messagesMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public MutableLiveData<ErrorResponse> getErrorResponseMutableLiveData() {
        return errorResponseMutableLiveData;
    }

    @NotNull
    private void getMutableLiveData(Call<MessageResponse> call) {
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();
                    messagesMutableLiveData.setValue(messageResponse.getMessageItems());
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
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("failed network message: ", t.getMessage());
            }
        });
    }

    public void getMessages(String conversationId) {
        loadingLiveData.setValue(true);
        MessageService messageService = RetrofitInstance.getMessageService(application.getApplicationContext());
        Call<MessageResponse> call = messageService.getMessages(conversationId);
        getMutableLiveData(call);
    }

    public void sendMessage(String conversationId, MessageItem message) {
        loadingLiveData.setValue(true);
        MessageService messageService = RetrofitInstance.getMessageService(application.getApplicationContext());
        Call<MessageResponse> call = messageService.sendMessage(conversationId, message);
        getMutableLiveData(call);
    }
}
