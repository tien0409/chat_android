package com.example.chatapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.chatapp.adapters.ChatAdapter;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.responses.conversation.ConversationItem;
import com.example.chatapp.responses.conversation.MembersItem;
import com.example.chatapp.responses.message.MessageItem;
import com.example.chatapp.responses.user.DataItem;
import com.example.chatapp.utils.PreferenceManager;
import com.example.chatapp.viewmodel.ConversationViewModel;
import com.example.chatapp.viewmodel.MainViewModel;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class ChatActivity extends AppCompatActivity {
    private String conversationId = null;
    private ActivityChatBinding binding;
    private DataItem receiverUser;
    private List<MessageItem> messageItemList;
    private PreferenceManager preferenceManager;
    private ChatAdapter chatAdapter;
    private ConversationViewModel conversationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        init();
        observerViewModel();
        setContentView(binding.getRoot());
        setListeners();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        conversationViewModel.destroy();
//    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        messageItemList = new ArrayList<>();
        conversationViewModel = ViewModelProviders.of(this).get(ConversationViewModel.class);

        loadReceiverDetails();
        loading(true);
        chatAdapter = new ChatAdapter(
                messageItemList,
                getBitmapFromEncodedString(receiverUser.getAvatar()),
                preferenceManager.getAuth().getId());
        binding.rcChat.setAdapter(chatAdapter);
//        createConversation(receiverUser.getId());
    }

    private void observerViewModel() {
        conversationViewModel.start();
        conversationViewModel.onSocketConnect.observe(this, this::onSocketConnect);
        conversationViewModel.onSocketGetConversation.observe(this, this::onSocketGetConversation);
        conversationViewModel.onSocketGetMessages.observe(this, this::onSocketGetMessages);
        conversationViewModel.onSocketSendMessage.observe(this, this::onSocketSendMessage);


        conversationViewModel.socketEmit("client-get-conversation", _conversationItem());
//        conversationViewModel.conversationRepository.getLoadingLiveData().observe(this, status -> {
//            loading(status);
//        });
//        conversationViewModel.conversationRepository.getConversationMutableLiveData().observe(this, conversation -> {
//            if (conversation != null && !conversation.getId().isEmpty()) {
//                conversationId = conversation.getId();
//                getMessages(conversation.getId());
//            }
//        });
//        conversationViewModel.messageRepository.getMessagesMutableLiveData().observe(this, messages -> {
//            messageItemList = messages;
//            chatAdapter = new ChatAdapter(
//                    messageItemList,
//                    getBitmapFromEncodedString(receiverUser.getAvatar()),
//                    preferenceManager.getAuth().getId());
//            binding.rcChat.setAdapter(chatAdapter);
//        });
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rcChat.setVisibility(View.INVISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.rcChat.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void loadReceiverDetails() {
        receiverUser = (DataItem) getIntent().getSerializableExtra("user");
        binding.txtUsername.setText(receiverUser.getUsername());
    }

//    private void createConversation(String receiverId) {
//        conversationViewModel.createConversation(receiverId);
//    }
//
//
//    private void getMessages(String conversationId) {
//        conversationViewModel.getMessages(conversationId);
//    }

    private void sendMessage() {
        MessageItem message = new MessageItem();
        message.setContent(binding.edInputMEssage.getText().toString());
        message.setAvatar(preferenceManager.getAuth().getAvatar());
        message.setConversation(conversationId);
        message.setSender(preferenceManager.getAuth().getId());
        message.setRoom(preferenceManager.getAuth().getId() + "-" + receiverUser.getId());
//            conversationViewModel.sendMessage(conversationId, message);
        conversationViewModel.socketEmit("client-send-message", message);
        binding.edInputMEssage.setText("");
    }

    private void onSocketConnect(Boolean aBoolean) {
        if (!aBoolean) return;

        if (null != preferenceManager.getAuth().getId())
            conversationViewModel.socketEmit("client-online", preferenceManager.getAuth().getUsername());
    }

    private void onSocketGetMessages(JSONArray messages) {
        Gson gson = new Gson();
        MessageItem[] messageItems = gson.fromJson(messages.toString(), MessageItem[].class);
        Collections.addAll(messageItemList, messageItems);
        chatAdapter.notifyDataSetChanged();
        loading(false);
    }

    private void onSocketGetConversation(String conversationId) {
        this.conversationId = conversationId;
        ConversationItem _con = _conversationItem();
        _con.setId(conversationId);
        conversationViewModel.socketEmit("client-get-messages", _con);
    }

    private void onSocketSendMessage(JSONObject message) {
        Gson gson = new Gson();
        MessageItem messageItem = gson.fromJson(message.toString(), MessageItem.class);
        messageItemList.add(messageItem);
        chatAdapter.notifyDataSetChanged();
        binding.rcChat.smoothScrollToPosition(messageItemList.size() - 1);
    }

    private ConversationItem _conversationItem() {
        ConversationItem _conversationItem = new ConversationItem();
        List<MembersItem> _membersConversation = new ArrayList<>();
        _membersConversation.add(new MembersItem(receiverUser.getId()));
        _membersConversation.add(new MembersItem(preferenceManager.getAuth().getId()));
        _conversationItem.setMembers(_membersConversation);
        return _conversationItem;
    }

}