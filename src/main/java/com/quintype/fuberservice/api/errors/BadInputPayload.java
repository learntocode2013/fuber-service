package com.quintype.fuberservice.api.errors;

public class BadInputPayload {
    private String message;

    public BadInputPayload() {
    }

    public BadInputPayload(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
