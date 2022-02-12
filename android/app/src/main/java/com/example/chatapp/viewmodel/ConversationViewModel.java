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

public class ConversationViewModel extends AndroidViewModel {
    //    public final ConversationRepository conversationRepository;
//    public final MessageRepository messageRepository;
    public final MutableLiveData<Boolean> onSocketConnect = new MutableLiveData<>();
    public final MutableLiveData<Boolean> onSocketDisconnect = new MutableLiveData<>();
    public final MutableLiveData<Boolean> onSocketConnectError = new MutableLiveData<>();
    public final MutableLiveData<String> onSocketGetConversation = new MutableLiveData<>();
    public final MutableLiveData<JSONArray> onSocketGetMessages = new MutableLiveData<>();
    public final MutableLiveData<JSONObject> onSocketSendMessage = new MutableLiveData<>();
    private final Emitter.Listener onConnect = args -> onSocketConnect.postValue(true);
    private final Emitter.Listener onDisconnect = args -> onSocketDisconnect.postValue(true);
    private final Emitter.Listener onConnectError = args -> onSocketConnectError.postValue(true);
    private final Emitter.Listener onGetConversation = args -> onSocketGetConversation.postValue((String) args[0]);
    private final Emitter.Listener onGetAllMessage = args -> onSocketGetMessages.postValue((JSONArray) args[0]);
    private final Emitter.Listener onSendMessage = args -> onSocketSendMessage.postValue((JSONObject) args[0]);
    @Inject
    SocketHelper mSocketHelper = new SocketHelper();
    private Socket mSocket;

    public ConversationViewModel(@NonNull @NotNull Application application) {
        super(application);
//        conversationRepository = new ConversationRepository(application);
//        messageRepository = new MessageRepository(application);
    }

//    public void getAllConversation() {
//        conversationRepository.getAllConversation();
//    }

//    public void createConversation(String receiverId) {
//        conversationRepository.createConversation(receiverId);
//    }
//
//    public void getMessages(String conversationId) {
//        messageRepository.getMessages(conversationId);
//    }


//    public void sendMessage(String conversationId, MessageItem message) {
//        messageRepository.sendMessage(conversationId, message);
//    }

    public void start() {
        mSocket = mSocketHelper.getSocket();

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on("server-send-messages", onGetAllMessage);
        mSocket.on("server-send-conversation", onGetConversation);
        mSocket.on("server-send-message", onSendMessage);
        mSocket.connect();
    }

    public void destroy() {
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off("server-send-messages", onGetAllMessage);
        mSocket.off("server-send-conversation", onGetConversation);
        mSocket.off("server-send-message", onSendMessage);
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
