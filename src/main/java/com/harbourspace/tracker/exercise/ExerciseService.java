package com.harbourspace.tracker.exercise;

import com.harbourspace.tracker.exercise.model.Exercise;
import com.harbourspace.tracker.exercise.model.NewExercise;

import java.util.List;

public interface ExerciseService {

    List<Exercise> getExerciseByUserId();

    Exercise getExerciseByExerciseId(long id);

    Exercise createExercise(NewExercise exercise);

    Exercise updateExercise(Exercise exercise);

    void deleteExercise(long id);

}
