package com.harbourspace.tracker.activity.model;

public record Activity(
        Long id,
        Long userId,
        String type,
        String name,
        double kcalPerMinute
) {
    public Activity copyWithId(Long id) {
        return new Activity(id, userId, type, name, kcalPerMinute);
    }
}
