package com.example.chatapp.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.chatapp.repositories.AuthRepository;
import com.example.chatapp.responses.auth.Data;
import org.jetbrains.annotations.NotNull;

public class AuthViewModel extends AndroidViewModel {
    public final AuthRepository authRepository;

    public AuthViewModel(@NotNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public LiveData<Data> signIn(String email, String password) {
        return authRepository.signIn(email, password);
    }

    public LiveData<Data> signUp(Data userModel) {
        return authRepository.signUp(userModel);
    }
}
