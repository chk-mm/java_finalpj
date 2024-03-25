package com.harbourspace.tracker.exercise.jpa;

import com.harbourspace.tracker.activity.jpa.ActivityEntity;
import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.error.AuthorizationException;
import com.harbourspace.tracker.error.NotFoundException;
import com.harbourspace.tracker.exercise.ExerciseService;
import com.harbourspace.tracker.exercise.model.Exercise;
import com.harbourspace.tracker.exercise.model.NewExercise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    public Exercise getExerciseByExerciseId(long id) {
        logger.debug("Getting exercise id: " + id);
        if (authorizationService.isSystem()) {
            var entity = exerciseRepository.findById(id).orElseThrow(() ->
                    new NotFoundException("Exercise id: " + id + " not found")
            );
            return toExercise(entity);
        } else throw unauthorized();
    }

    @Override
    public Exercise createExercise(NewExercise exercise) {
        logger.debug("Creating new exercise: " + exercise);
        long userId = authorizationService.getCurrentUser().id();
        var entity = exerciseRepository.save(fromExercise(userId, exercise));
        return toExercise(entity);
    }

    @Override
    public Exercise updateExercise(Exercise exercise) {
        logger.debug("Updating exercise: " + exercise);

        long id = exercise.id();
        var temp = exerciseRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Exercise id: " + id + " not found")
        );

        long currentUserId = authorizationService.getCurrentUser().id();
        long userId = exerciseRepository.getReferenceById(exercise.id()).getUserId();
        if (currentUserId == userId) {
            long duration = exercise.duration();

            if (duration > 0) {
                temp.setDuration(exercise.duration());
            }

            var entity = exerciseRepository.save(temp);
            return toExercise(entity);
        } else throw unauthorized();
    }

    @Override
    public void deleteExercise(long id) {
        logger.debug("Deleting exercise " + id);
        exerciseRepository.delete(exerciseRepository.getReferenceById(id));
    }

    private AuthorizationException unauthorized() {
        var authorizationException = new AuthorizationException("Exercise is not authorized for this operation.");
        logger.error(authorizationException.getMessage());
        return authorizationException;
    }

    public static ExerciseEntity fromExercise(long userId, NewExercise exercise) {
        ExerciseEntity entity = new ExerciseEntity();
        entity.setUserId(userId);
        entity.setActivityId(exercise.activityId());
        entity.setStartTime(new Timestamp(System.currentTimeMillis()));
        entity.setDuration(exercise.duration());
        entity.setKcalBurned(new ActivityEntity().getKcalPerMinute() * exercise.duration());
        return entity;
    }

    public static Exercise toExercise(ExerciseEntity entity) {
        return new Exercise(entity.getId(), entity.getUserId(), entity.getActivityId(), entity.getStartTime(), entity.getDuration(), entity.getKcalBurned());
    }
}
