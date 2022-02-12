package com.example.chatapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.chatapp.utils.SocketHelper;
import com.google.gson.Gson;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

public class MainViewModel extends AndroidViewModel {
    public final MutableLiveData<Boolean> onSocketConnect = new MutableLiveData<>();
    public final MutableLiveData<Boolean> onSocketDisconnect = new MutableLiveData<>();
    public final MutableLiveData<Boolean> onSocketConnectError = new MutableLiveData<>();
    public final MutableLiveData<JSONArray> onSocketGetConversations = new MutableLiveData<>();
    public final MutableLiveData<JSONObject> onSocketSendMessage = new MutableLiveData<>();
    public final MutableLiveData<Boolean> onSocketSendFirstMessage = new MutableLiveData<>();
    private final Emitter.Listener onConnect = args -> onSocketConnect.postValue(true);
    private final Emitter.Listener onDisconnect = args -> onSocketDisconnect.postValue(true);
    private final Emitter.Listener onConnectError = args -> onSocketConnectError.postValue(true);
    private final Emitter.Listener onGetConversations = args -> onSocketGetConversations.postValue((JSONArray) args[0]);
    private final Emitter.Listener onSendMessage = args -> onSocketSendMessage.postValue((JSONObject) args[0]);
    private final Emitter.Listener onSendFirstMessage = args -> onSocketSendFirstMessage.postValue((Boolean) args[0]);
    @Inject
    SocketHelper mSocketHelper = new SocketHelper();
    private Socket mSocket;

    public MainViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void start() {
        mSocket = mSocketHelper.getSocket();

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on("server-send-message", onSendMessage);
        mSocket.on("server-send-conversations", onGetConversations);
        mSocket.on("server-send-first-message", onSendFirstMessage);
        mSocket.connect();
    }

    public void destroy() {
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off("server-send-message", onGetConversations);
        mSocket.off("server-send-conversations", onGetConversations);
        mSocket.off("server-send-first-message", onGetConversations);
    }

    public Socket getSocket() {
        return mSocket;
    }

    public void socketEmit(String param) {
        socketEmit(param, null);
    }

    public void socketEmit(String param, Object message) {
        Gson gson = new Gson();
        if (message == null) mSocket.emit(param);
        else mSocket.emit(param, gson.toJson(message));
    }
}
