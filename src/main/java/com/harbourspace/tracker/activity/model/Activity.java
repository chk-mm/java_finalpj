package com.harbourspace.tracker.activity.model;

public record Activity(
        Long id,                // Unique identifier of the activity.
        Long userId,            // The unique identifier of the user who added the activity (0 for SYSTEM)
        String type,            // The type of the activity (SYSTEM or USER).
        String name,            // The name of the activity.
        double kcalPerMinute    // The number of kilocalories burned per minute during the activity.
) {
    public Activity copyWithId(Long id) {
        return new Activity(id, userId, type, name, kcalPerMinute);
    }
}
