package com.harbourspace.tracker.exercise.jpa;

import com.harbourspace.tracker.exercise.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseJpaRepository extends JpaRepository<ExerciseEntity, Long> {

    List<ExerciseEntity> findByUserId(long userId);

}
