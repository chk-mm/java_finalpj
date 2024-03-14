package com.harbourspace.tracker.user.model;

public record NewUser(
        String name
) {
    public User toUser(long id) {
        return new User(id, name);
    }
}
