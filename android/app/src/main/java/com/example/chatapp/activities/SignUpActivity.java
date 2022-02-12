package com.example.chatapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.chatapp.databinding.ActivitySignUpBinding;
import com.example.chatapp.responses.auth.Data;
import com.example.chatapp.utils.Image;
import com.example.chatapp.utils.PreferenceManager;
import com.example.chatapp.viewmodel.AuthViewModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String encodedImage;
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.txtAddImage.setVisibility(View.GONE);
                            encodedImage = Image.encodedImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private AuthViewModel authViewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(binding.getRoot());
        setListeners();
    }

    private void init() {
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        authViewModel.authRepository.getLoadingLiveData().observe(this, status -> {
            loading(status);
        });
        authViewModel.authRepository.getErrorResponseMutableLiveData().observe(this, error -> {
            showToast(error.message);
        });
    }

    private void setListeners() {
        binding.txtSignIn.setOnClickListener(v -> onBackPressed());
        binding.btnSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails()) {
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void signUp() {
        String email = binding.edEmail.getText().toString();
        String password = binding.edPassword.getText().toString();
        String phoneNumber = binding.edPhoneNumber.getText().toString();
        String fullName = binding.edFullName.getText().toString();
        String username = binding.edUsername.getText().toString();
        Data userModel = new Data();
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setUsername(username);
        userModel.setFullName(fullName);
        userModel.setPhoneNumber(phoneNumber);
        userModel.setAvatar(encodedImage);

        authViewModel.signUp(userModel).observe(this, user -> {
            Log.d("user", user.toString());
            preferenceManager.setAuth(user);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignUpDetails() {
        if (encodedImage == null) {
            showToast("Select profile image");
            return false;
        } else if (binding.edEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (binding.edPhoneNumber.getText().toString().trim().isEmpty()) {
            showToast("Enter phone number");
            return false;
        } else if (binding.edFullName.getText().toString().trim().isEmpty()) {
            showToast("Enter full name");
            return false;
        } else if (binding.edUsername.getText().toString().trim().isEmpty()) {
            showToast("Enter username");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (binding.edPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (binding.edConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter confirm password");
            return false;
        } else if (!binding.edPassword.getText().toString().equals(binding.edConfirmPassword.getText().toString())) {
            showToast("Password and confirm password must be same");
            return false;
        } else {
            return true;
        }
    }
}