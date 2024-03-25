package com.harbourspace.tracker.activity.model;

public record NewActivity(
        Long userId,
        String type,
        String name,
        double kcalPerMinute
) {
    public Activity toActivity(long id) {
        return new Activity(id, userId, type, name, kcalPerMinute);
    }
}
