package com.harbourspace.tracker.error;

public record ErrorResponse(
        String error,
        int status
)
{ }
