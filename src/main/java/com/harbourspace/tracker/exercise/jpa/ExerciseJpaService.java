package com.harbourspace.tracker.exercise.jpa;

import com.harbourspace.tracker.activity.jpa.ActivityEntity;
import com.harbourspace.tracker.activity.jpa.ActivityJpaService;
import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.error.AuthorizationException;
import com.harbourspace.tracker.error.NotFoundException;
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
    public List<Exercise> getExerciseByUserId() {
        long userId = authorizationService.getCurrentUser().id();
        logger.debug("Getting all exercise of user id: " + userId);
        List<ExerciseEntity> entities = exerciseRepository.findByUserId(userId);
        return entities.stream().map(ExerciseJpaService::toExercise).collect(Collectors.toList());
    }

    @Override
    public Exercise getExerciseById(long id) {
        logger.debug("Getting exercise id: " + id);
        if (authorizationService.isSystem()) {
            var entity = exerciseRepository.findById(id).orElseThrow(() ->
                    new NotFoundException("Exercises id: " + id + " not found")
            );
            return toExercise(entity);
        } else throw unauthorized();
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
