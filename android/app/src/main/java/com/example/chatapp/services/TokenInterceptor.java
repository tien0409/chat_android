package com.example.chatapp.services;

import android.content.Context;
import android.util.Log;
import com.example.chatapp.utils.PreferenceManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TokenInterceptor implements Interceptor {
    private final PreferenceManager preferenceManager;

    public TokenInterceptor(Context context) {
        preferenceManager = new PreferenceManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (preferenceManager != null && preferenceManager.getAuth().getToken() != null && !preferenceManager.getAuth().getToken().trim().isEmpty()) {
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + preferenceManager.getAuth().getToken())
                    .build();
        }

        return chain.proceed(request);
    }
}
