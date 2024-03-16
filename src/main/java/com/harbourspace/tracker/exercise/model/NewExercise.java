package com.harbourspace.tracker.exercise.model;

import java.sql.Timestamp;

public record NewExercise(
        Long userId,            // The unique identifier of the user who added the activity.
        Long activityId,        // The unique identifier of the activity.
        Timestamp startTime,    // The date and time the activity was started.
        Long duration           // The duration of the activity in seconds.
) {
    public Exercise toExercise(long id) {
        return new Exercise(id, userId, activityId, startTime, duration, 0.0);
    }
}
