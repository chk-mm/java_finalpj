package com.harbourspace.tracker.error;


public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException() {
        super("Conflict");
    }

}

