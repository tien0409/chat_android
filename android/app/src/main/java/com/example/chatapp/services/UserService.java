package com.example.chatapp.services;

import com.example.chatapp.responses.UsersResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("user")
    Call<UsersResponse> getUsers();
}
