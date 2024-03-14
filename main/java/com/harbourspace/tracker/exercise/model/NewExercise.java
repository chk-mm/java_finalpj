package com.harbourspace.tracker.exercise.model;

import java.sql.Timestamp;

public record NewExercise(
        Long userId,
        Long activityId,
        Timestamp startTime,
        Long duration
) {
    public Exercise toExercise(long id) {
        return new Exercise(id, userId, activityId, startTime, duration, 0.0);
    }
}
