package com.example.chatapp.listeners;

public interface SocketListener {
    void onConnect();

    void onConnectError();

    void onReconnect();

    void onDisconnect();

    void onSendMessage();

    void onGetMessages();
}
