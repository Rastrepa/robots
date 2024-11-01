package com.example.robots.model;

import lombok.Getter;

@Getter
public abstract class ResponseMessage {
    protected final String status;

    public ResponseMessage(String status) {
        this.status = status;
    }
}

class ErrorResponse extends ResponseMessage {
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        super("ERROR");
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getStatus(), errorMessage);
    }
}