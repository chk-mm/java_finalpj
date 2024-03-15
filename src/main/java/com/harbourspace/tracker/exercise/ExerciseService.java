package com.harbourspace.tracker.exercise;

import com.harbourspace.tracker.exercise.model.Exercise;
import com.harbourspace.tracker.exercise.model.NewExercise;

import java.util.List;

public interface ExerciseService {

    List<Exercise> getExercises();

    List<Exercise> getExerciseById(long id);

    List<Exercise> getExerciseByUserId(long userId);

    List<Exercise> getExerciseByActivity(long activityId);

    List<Exercise> getExerciseByDuration(long duration);

    Exercise createExercise(NewExercise exercise);

    Exercise updateExercise(Exercise exercise);

    void deleteExercise(long userId);
}
