package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.chatapp.adapters.UsersAdapter;
import com.example.chatapp.databinding.ActivityUsersBinding;
import com.example.chatapp.listeners.UserListener;
import com.example.chatapp.responses.user.DataItem;
import com.example.chatapp.utils.PreferenceManager;
import com.example.chatapp.viewmodel.UserViewModel;

public class UsersActivity extends AppCompatActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(binding.getRoot());
        setListeners();
    }

    private void init() {
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.userRepository.getLoadingLiveData().observe(this, status -> {
            loading(status);
        });

        userViewModel.userRepository.getErrorResponseMutableLiveData().observe(this, error -> {
            showError(error.message);
        });

        userViewModel.userRepository.getUsers().observe(this, users -> {
            if (users.getData().size() > 0) {
                UsersAdapter usersAdapter = new UsersAdapter(users.getData(), this);
                binding.rcUsers.setAdapter(usersAdapter);
                binding.rcUsers.setVisibility(View.VISIBLE);
            } else {
                showError("No user available");
            }
        });
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void showError(String message) {
        binding.txtError.setText(message);
        binding.txtError.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClicked(DataItem user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}