package com.harbourspace.tracker.exercise.model;

import java.sql.Timestamp;

public record Exercise(
        Long id,                // The unique identifier of the user activity.
        Long userId,            // The unique identifier of the user who added the activity.
        Long activityId,        // The unique identifier of the activity.
        Timestamp startTime,    // The date and time the activity was started.
        Long duration,          // The duration of the activity in seconds.
        double kcalBurned       // The number of kilocalories burned during the activity. This is a calculated number based on duration and activity type.
) {
    public Exercise copyWithId(Long id) {
        return new Exercise(id, userId, activityId, startTime, duration, kcalBurned);
    }
}
