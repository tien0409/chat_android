package com.example.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.chatapp.responses.auth.Data;
import com.google.gson.Gson;

public class PreferenceManager {
    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constant.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setAuth(Data user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString("loggedInUser", userJson);
        editor.apply();
    }

    public Data getAuth() {
        Gson gson = new Gson();
        String userJson = sharedPreferences.getString("loggedInUser", null);
        return gson.fromJson(userJson, Data.class);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
