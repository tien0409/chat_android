package com.example.chatapp.listeners;

import com.example.chatapp.responses.user.DataItem;

public interface ConversationListener {
    void onConversationClicked(DataItem user);
}
