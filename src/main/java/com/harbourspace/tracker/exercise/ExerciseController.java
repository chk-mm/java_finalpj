package com.harbourspace.tracker.exercise;

import com.harbourspace.tracker.exercise.model.Exercise;
import com.harbourspace.tracker.exercise.model.NewExercise;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping()
    ResponseEntity<List<Exercise>> getExerciseByUserId() {
        return ResponseEntity.ok(exerciseService.getExerciseByUserId());
    }

    @GetMapping("{id}")
    ResponseEntity<Exercise> getExerciseById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(exerciseService.getExerciseById(id));
    }

    @PostMapping
    ResponseEntity<Exercise> createExercise(@RequestBody NewExercise exercise) {
        return new ResponseEntity<>(exerciseService.createExercise(exercise), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    ResponseEntity<Exercise> updateExercise(@PathVariable("id") Long id, @RequestBody Exercise exercise) {
        return ResponseEntity.ok(exerciseService.updateExercise(exercise.copyWithId(id)));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Object> deleteExercise(@PathVariable("id") Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.ok().build();
    }
}
