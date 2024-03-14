package com.harbourspace.tracker.activity.jpa;

import com.harbourspace.tracker.activity.ActivityService;
import com.harbourspace.tracker.activity.model.Activity;
import com.harbourspace.tracker.activity.model.NewActivity;
import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.error.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Activity> getActivity() {
        if (authorizationService.isSystem()) {
            logger.debug("Getting all activity");
            return activityRepository.findAll().stream().map(ActivityJpaService::toActivity).toList();
        } else throw unauthorized();
    }

    @Override
    public List<Activity> getActivityByUserId(long userId) {
        if (authorizationService.isSystem()) {
            logger.debug("Getting all activity of user: " + userId);
            return activityRepository.findById(userId).stream().map(ActivityJpaService::toActivity).toList();
        } else throw unauthorized();
    }

    @Override
    public Activity createActivity(NewActivity activity) {
        if (authorizationService.isSystem()) {
            logger.debug("Creating new activity: " + activity);
            var entity = activityRepository.save(fromActivity(activity));
            return toActivity(entity);
        } else throw unauthorized();
    }

    @Override
    public Activity updateActivity(Activity activity) {
        if (authorizationService.isSystem()) {
            logger.debug("Updating activity: " + activity);
            var entity = activityRepository.save(fromActivity(activity));
            return toActivity(entity);
        } else throw unauthorized();
    }

    @Override
    public void deleteActivity(long id) {
        if (authorizationService.isSystem()) {
            logger.debug("Deleting activity " + id);
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
