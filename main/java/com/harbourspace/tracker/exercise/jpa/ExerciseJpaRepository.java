package com.harbourspace.tracker.exercise.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseJpaRepository extends JpaRepository<ExerciseEntity, Long> {

    List<ExerciseEntity> findByUserId(long userId);

}
