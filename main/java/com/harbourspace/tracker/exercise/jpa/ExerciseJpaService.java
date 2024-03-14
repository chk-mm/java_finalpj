package com.harbourspace.tracker.exercise.jpa;

import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.error.AuthorizationException;
import com.harbourspace.tracker.exercise.ExerciseService;
import com.harbourspace.tracker.exercise.model.NewExercise;
import com.harbourspace.tracker.exercise.model.Exercise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class ExerciseJpaService implements ExerciseService {

    private final Logger logger = LoggerFactory.getLogger(ExerciseJpaService.class);

    private final ExerciseJpaRepository exerciseRepository;
    private final AuthorizationService authorizationService;

    public ExerciseJpaService(ExerciseJpaRepository exerciseRepository, AuthorizationService authorizationService) {
        this.exerciseRepository = exerciseRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public List<Exercise> getExercises() {
        if (authorizationService.isSystem()) {
            logger.debug("Getting all exercises");
            return exerciseRepository.findAll().stream().map(ExerciseJpaService::toExercise).toList();
        } else throw unauthorized();
    }

    @Override
    public List<Exercise> getExerciseByUserId(long userId) {
        if (authorizationService.isSystem()) {
            logger.debug("Getting exercise of user: " + userId);
            List<ExerciseEntity> entityList = exerciseRepository.findByUserId(userId);
            return entityList.stream().map(ExerciseJpaService::toExercise).collect(Collectors.toList());

        } else throw unauthorized();
    }

    @Override
    public List<Exercise> getExerciseByDate() {
        return null;
    }

    @Override
    public List<Exercise> getExerciseByActivity(long activityId) {
        return null;
    }

    @Override
    public List<Exercise> getExerciseByDuration(long duration) {
        return null;
    }

    @Override
    public Exercise createExercise(NewExercise exercise) {
        if (authorizationService.isSystem()) {
            logger.debug("Creating new exercise: " + exercise);
            var entity = exerciseRepository.save(fromExercise(exercise));
            return toExercise(entity);
        } else throw unauthorized();
    }

    @Override
    public Exercise updateExercise(Exercise exercise) {
        if (authorizationService.isSystem()) {
            logger.debug("Updating exercise: " + exercise);
            var entity = exerciseRepository.save(fromExercise(exercise));
            return toExercise(entity);
        } else throw unauthorized();
    }

    @Override
    public void deleteExercise(long id) {
        if (authorizationService.isSystem()) {
            logger.debug("Deleting exercise " + id);
            exerciseRepository.delete(exerciseRepository.getReferenceById(id));
        } else throw unauthorized();
    }

    private AuthorizationException unauthorized() {
        var authorizationException = new AuthorizationException("Exercise is not authorized for this operation.");
        logger.error(authorizationException.getMessage());
        return authorizationException;
    }

    public static ExerciseEntity fromExercise(Exercise exercise) {
        ExerciseEntity entity = new ExerciseEntity();
        entity.setId(exercise.id());
        entity.setUserId(exercise.userId());
        return entity;
    }

    public static ExerciseEntity fromExercise(NewExercise exercise) {
        ExerciseEntity entity = new ExerciseEntity();
        entity.setUserId(exercise.userId());
        return entity;
    }

    public static Exercise toExercise(ExerciseEntity entity) {
        return new Exercise(entity.getId(), entity.getUserId(), entity.getActivityId(), entity.getStartTime(), entity.getDuration(), entity.getKcalBurned());
    }
}
