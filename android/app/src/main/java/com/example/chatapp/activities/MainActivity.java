package com.example.chatapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.chatapp.adapters.RecentConversationAdapter;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.listeners.ConversationListener;
import com.example.chatapp.responses.conversation.ConversationItem;
import com.example.chatapp.responses.user.DataItem;
import com.example.chatapp.utils.PreferenceManager;
import com.example.chatapp.viewmodel.ConversationViewModel;
import com.example.chatapp.viewmodel.MainViewModel;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ConversationListener {
    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    private List<ConversationItem> conversations;
    private RecentConversationAdapter conversationAdapter;
    private ConversationViewModel conversationViewModel;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        observerViewModel();
        setContentView(binding.getRoot());
        setListeners();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mainViewModel.socketEmit("client-get-conversations", preferenceManager.getAuth().getId());
//    }

    private void init() {
        conversations = new ArrayList<>();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        loading(true);
        conversationAdapter = new RecentConversationAdapter(conversations, preferenceManager.getAuth().getId(), this::onConversationClicked);

//        conversationViewModel = ViewModelProviders.of(this).get(ConversationViewModel.class);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        binding.rcConversation.setAdapter(conversationAdapter);
//        getAllConversations();
    }


    private void observerViewModel() {
//        conversationViewModel.conversationRepository.getAllConversationMutableLiveData().observe(this, conversationItems -> {
//            if (conversationItems.size() > 0) {
//                conversations.clear();
//                conversations.addAll(conversationItems);
//                binding.rcConversation.setVisibility(View.VISIBLE);
//                binding.rcConversation.smoothScrollToPosition(conversations.size() - 1);
//                binding.progressBar.setVisibility(View.GONE);
//            }
//        });
        mainViewModel.start();
        mainViewModel.onSocketConnect.observe(this, this::onSocketConnect);
        mainViewModel.onSocketSendMessage.observe(this, this::onSocketSendMessage);
        mainViewModel.onSocketSendFirstMessage.observe(this, this::onSocketSendFirstMessage);
        mainViewModel.onSocketGetConversations.observe(this, this::onSocketGetConversations);

        mainViewModel.socketEmit("client-get-conversations", preferenceManager.getAuth().getId());
    }


    private void setListeners() {
        binding.imageSignOut.setOnClickListener(v -> signOut());
        binding.fabNewChat.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UsersActivity.class));
        });
    }

    private void loadUserDetails() {
        binding.txtUsername.setText(preferenceManager.getAuth().getUsername());
        byte[] bytes = Base64.decode(preferenceManager.getAuth().getAvatar(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

//    private void getAllConversations() {
//        conversationViewModel.getAllConversation();
//    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rcConversation.setVisibility(View.INVISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.rcConversation.setVisibility(View.VISIBLE);
        }
    }

    private void signOut() {
        preferenceManager.clear();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }

    @Override
    public void onConversationClicked(DataItem user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void onSocketConnect(Boolean aBoolean) {
        if (!aBoolean) return;

        if (null != preferenceManager.getAuth().getId())
            mainViewModel.socketEmit("client-online", preferenceManager.getAuth().getId());
    }

    private void onSocketGetConversations(JSONArray conversations) {
        Gson gson = new Gson();
        ConversationItem[] conversationItems = gson.fromJson(conversations.toString(), ConversationItem[].class);
        this.conversations.clear();
        Collections.addAll(this.conversations, conversationItems);
        conversationAdapter.notifyDataSetChanged();
        loading(false);
    }

    private void onSocketSendMessage(JSONObject message) {
        Log.d("sent", "send message");
        mainViewModel.socketEmit("client-get-conversations", preferenceManager.getAuth().getId());
    }

    private void onSocketSendFirstMessage(Boolean status) {
        Log.d("first", "first message");
        if (status) {
            mainViewModel.socketEmit("client-get-conversations", preferenceManager.getAuth().getId());
        }
    }
}