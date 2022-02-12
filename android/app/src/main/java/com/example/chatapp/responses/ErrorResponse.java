package com.example.chatapp.responses;

public class ErrorResponse {
    public int code;
    public String message;

    public ErrorResponse() {
    }

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
