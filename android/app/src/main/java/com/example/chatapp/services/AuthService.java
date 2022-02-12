package com.example.chatapp.services;

import com.example.chatapp.responses.auth.AuthResponse;
import com.example.chatapp.responses.auth.Data;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {
    @POST("user/signin")
    @FormUrlEncoded
    Call<AuthResponse> signIn(@Field("email") String email, @Field("password") String password);

    @POST("user/signup")
    Call<AuthResponse> signUp(@Body Data userModel);
}
