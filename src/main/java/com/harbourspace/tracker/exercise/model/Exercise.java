package com.harbourspace.tracker.exercise.model;

import java.sql.Timestamp;

public record Exercise(
        Long id,
        Long userId,
        Long activityId,
        Timestamp startTime,
        Long duration,
        double kcalBurned
) {
    public Exercise copyWithId(Long id) {
        return new Exercise(id, userId, activityId, startTime, duration, kcalBurned);
    }
}
