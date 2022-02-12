package com.example.chatapp.services;

import android.content.Context;
import com.example.chatapp.utils.Constant;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofit = retrofitBuilder.build();

    public static AuthService getAuthService() {
        return retrofit.create(AuthService.class);
    }

    public static UserService getUserService(Context context) {
        return retrofitBuilder.client(makeOkHttpClient(context)).build().create(UserService.class);
    }

    public static ConversationService getConversationService(Context context) {
        return retrofitBuilder.client(makeOkHttpClient(context)).build().create(ConversationService.class);
    }

    public static MessageService getMessageService(Context context) {
        return retrofitBuilder.client(makeOkHttpClient(context)).build().create(MessageService.class);
    }

    private static OkHttpClient makeOkHttpClient(Context context) {
        return new OkHttpClient.Builder().addInterceptor(new TokenInterceptor(context)).build();
    }

}
