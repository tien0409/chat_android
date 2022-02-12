package com.example.chatapp.repositories;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.chatapp.responses.ErrorResponse;
import com.example.chatapp.responses.UsersResponse;
import com.example.chatapp.services.RetrofitInstance;
import com.example.chatapp.services.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class UserRepository {
    private final MutableLiveData<UsersResponse> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<ErrorResponse> errorResponseMutableLiveData = new MutableLiveData<>();
    private final Application application;

    public UserRepository(Application application) {
        this.application = application;
    }


    public MutableLiveData<UsersResponse> getUsersLiveData() {
        return usersLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public MutableLiveData<ErrorResponse> getErrorResponseMutableLiveData() {
        return errorResponseMutableLiveData;
    }

    @NotNull
    private MutableLiveData<UsersResponse> getMutableLiveData(Call<UsersResponse> call) {
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful()) {
                    UsersResponse usersResponse = response.body();
                    usersLiveData.setValue(usersResponse);
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

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Log.d("failed network user: ", t.getMessage());
            }
        });

        return usersLiveData;
    }

    public MutableLiveData<UsersResponse> getUsers() {
        loadingLiveData.setValue(true);
        UserService userService = RetrofitInstance.getUserService(application.getApplicationContext());
        Call<UsersResponse> call = userService.getUsers();
        return getMutableLiveData(call);
    }
}
