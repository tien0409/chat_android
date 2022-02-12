package com.example.chatapp.services;

import android.util.Log;
import com.example.chatapp.listeners.SocketListener;
import com.example.chatapp.utils.BaseObservable;
import com.example.chatapp.utils.Constant;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends BaseObservable<SocketListener> {

    private static final String TAG = SocketService.class.getSimpleName();

    private static final String SOCKET_URL = Constant.BASE_URL;
    private static final String EVENT_CONNECT = Socket.EVENT_CONNECT;
    private static final String EVENT_CONNECT_ERROR = Socket.EVENT_CONNECT_ERROR;
    private static final String EVENT_DISCONNECT = Socket.EVENT_DISCONNECT;
    private static final String EVENT_SEND_MESSAGE = "sendMessage";
    private static final String EVENT_GET_MESSAGES = "client-get-all-message";
    private final Emitter.Listener connectListener = args -> {
        Log.d(TAG, "onConnect ...");
        for (SocketListener listener : getListeners())
            listener.onConnect();
    };
    private final Emitter.Listener reconnectListener = args -> {
        Log.d(TAG, "onReconnect ...");
        for (SocketListener listener : getListeners())
            listener.onReconnect();
    };
    private final Emitter.Listener connectionErrorListener = args -> {
        Log.d(TAG, "onConnectionError...");
        for (SocketListener listener : getListeners()) {
            listener.onConnectError();
        }
    };
    private Socket socket;
    private final Emitter.Listener disconnectListener = args -> {
        Log.d(TAG, "onDisconnect ...");
        socket.off();
        for (SocketListener listener : getListeners())
            listener.onDisconnect();
    };

    private String username;
}