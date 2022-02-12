package com.example.chatapp.repositories;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.chatapp.responses.ErrorResponse;
import com.example.chatapp.responses.auth.AuthResponse;
import com.example.chatapp.responses.auth.Data;
import com.example.chatapp.services.AuthService;
import com.example.chatapp.services.RetrofitInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class AuthRepository {
    private final MutableLiveData<Data> authLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<ErrorResponse> errorResponseMutableLiveData = new MutableLiveData<>();
    private final Application application;
    private Data auth = new Data();

    public AuthRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<Data> getAuthLiveData() {
        return authLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public MutableLiveData<ErrorResponse> getErrorResponseMutableLiveData() {
        return errorResponseMutableLiveData;
    }

    @NotNull
    private MutableLiveData<Data> getMutableLiveData(Call<AuthResponse> call) {
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    auth = authResponse.getData();
                    authLiveData.setValue(auth);
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
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.d("failed network auth: ", t.getMessage());
            }
        });

        return authLiveData;
    }

    public MutableLiveData<Data> signIn(String email, String password) {
        loadingLiveData.setValue(true);
        AuthService authService = RetrofitInstance.getAuthService();
        Call<AuthResponse> call = authService.signIn(email, password);
        return getMutableLiveData(call);
    }

    public MutableLiveData<Data> signUp(Data userModel) {
        loadingLiveData.setValue(true);
        AuthService authService = RetrofitInstance.getAuthService();
        Call<AuthResponse> call = authService.signUp(userModel);
        return getMutableLiveData(call);
    }

}
