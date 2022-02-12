package com.example.chatapp.responses;

public class NullSocketException extends Exception {
    private static final String MESSAGE = "Trying to use socket with a null pointer";

    private NullSocketException() {
        super(MESSAGE);
    }
}
