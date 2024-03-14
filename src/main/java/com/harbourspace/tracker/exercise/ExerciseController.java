package com.harbourspace.tracker.exercise;

import com.harbourspace.tracker.exercise.model.Exercise;
import com.harbourspace.tracker.exercise.model.NewExercise;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    ResponseEntity<List<Exercise>> getExercises() {
        return ResponseEntity.ok(exerciseService.getExercises());
    }

    @GetMapping("{userId}")
    ResponseEntity<List<Exercise>> getExerciseById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(exerciseService.getExerciseByUserId(userId));
    }

    @PostMapping
    ResponseEntity<Exercise> createExercise(@RequestBody NewExercise exercise) {
        return new ResponseEntity<>(exerciseService.createExercise(exercise), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    ResponseEntity<Exercise> updateExercise(
            @PathVariable("id") Long id,
            @RequestBody Exercise exercise
    ) {
        return ResponseEntity.ok(exerciseService.updateExercise(exercise.copyWithId(id)));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Object> deleteExercise(@PathVariable("id") Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.ok().build();
    }
}
