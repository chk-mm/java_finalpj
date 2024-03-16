package com.harbourspace.tracker.activity.jpa;

import com.harbourspace.tracker.activity.ActivityService;
import com.harbourspace.tracker.activity.model.Activity;
import com.harbourspace.tracker.activity.model.NewActivity;
import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.error.AuthorizationException;
import com.harbourspace.tracker.error.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class ActivityJpaService implements ActivityService {

    private final Logger logger = LoggerFactory.getLogger(ActivityJpaService.class);

    private final ActivityJpaRepository activityRepository;
    private final AuthorizationService authorizationService;

    public ActivityJpaService(ActivityJpaRepository activityRepository, AuthorizationService authorizationService) {
        this.activityRepository = activityRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public List<Activity> getActivityByUserId() {
        long userId = authorizationService.getCurrentUser().id();
        logger.debug("Getting all activity of user id: " + userId);
        List<ActivityEntity> entities = activityRepository.findByUserId(userId);
        return entities.stream().map(ActivityJpaService::toActivity).collect(Collectors.toList());
    }

    @Override
    public Activity getActivityByActivityId(long id) {
        logger.debug("Getting activity id: " + id);
        if (authorizationService.isSystem()) {
            var entity = activityRepository.findById(id).orElseThrow(() ->
                    new NotFoundException("Activity id: " + id + " not found")
            );
            return toActivity(entity);
        } else throw unauthorized();
    }

    @Override
    public Activity createActivity(long userId, NewActivity activity) {
        logger.debug("Creating new activity: " + activity);
        var entity = activityRepository.save(fromActivity(activity));
        return toActivity(entity);
    }

    @Override
    public Activity updateActivity(long userId, Activity activity) {
        logger.debug("Updating activity: " + activity);
        if (authorizationService.isSystem()) {
            var entity = activityRepository.save(fromActivity(activity));
            return toActivity(entity);
        } else throw unauthorized();
    }

    @Override
    public void deleteActivity(long userId, long id) {
        logger.debug("Deleting activity " + id);
        if (authorizationService.isSystem()) {
            activityRepository.delete(activityRepository.getReferenceById(id));
        } else throw unauthorized();
    }

    private AuthorizationException unauthorized() {
        var authorizationException = new AuthorizationException("Activity is not authorized for this operation.");
        logger.error(authorizationException.getMessage());
        return authorizationException;
    }

    public static ActivityEntity fromActivity(Activity activity) {
        ActivityEntity entity = new ActivityEntity();
        entity.setId(activity.id());
        entity.setName(activity.name());
        return entity;
    }

    public static ActivityEntity fromActivity(NewActivity activity) {
        ActivityEntity entity = new ActivityEntity();
        entity.setName(activity.name());
        return entity;
    }

    public static Activity toActivity(ActivityEntity entity) {
        return new Activity(entity.getId(), entity.getUserId(), entity.getType(), entity.getName(), entity.getKcalPerMinute());
    }

}
