package com.example.chatapp.utils;

import io.socket.client.IO;
import io.socket.client.Socket;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URISyntaxException;

@Singleton
public class SocketHelper {
    private static Socket socket = null;

    @Inject
    public SocketHelper() {
        if (this.getSocket() == null) {
            try {
                socket = IO.socket(Constant.SOCKET_BASE_URL);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}