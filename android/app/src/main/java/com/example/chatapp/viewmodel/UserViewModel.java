package com.example.chatapp.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.chatapp.repositories.UserRepository;
import com.example.chatapp.responses.UsersResponse;
import org.jetbrains.annotations.NotNull;

public class UserViewModel extends AndroidViewModel {
    public final UserRepository userRepository;

    public UserViewModel(@NotNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<UsersResponse> getUsers() {
        return userRepository.getUsers();
    }

}
