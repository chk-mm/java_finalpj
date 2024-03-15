package com.harbourspace.tracker.user.model;

public record User(
        Long id,
        String name
) {
    public User copyWithId(Long id) {
        return new User(id, name);
    }
}
