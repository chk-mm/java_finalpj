package com.harbourspace.tracker.error;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String message) {
        super(message);
    }
    public AuthorizationException() {
        super("Unauthorized access.");
    }

}
