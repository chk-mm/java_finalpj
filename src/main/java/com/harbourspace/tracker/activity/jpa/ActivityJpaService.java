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
            var entity = activityRepository.findById(id).orElseThrow(() -> new NotFoundException("Activity id: " + id + " not found"));
            return toActivity(entity);
        } else throw unauthorized();
    }

    @Override
    public Activity createActivity(NewActivity activity) {
        logger.debug("Creating new activity: " + activity);
        long userId = authorizationService.getCurrentUser().id();
        var entity = activityRepository.save(fromActivity(userId, activity));
        return toActivity(entity);
    }

    @Override
    public Activity updateActivity(Activity activity) {
        logger.debug("Updating activity: " + activity);

        long id = activity.id();
        var temp = activityRepository.findById(id).orElseThrow(() -> new NotFoundException("Activity id: " + id + " not found"));

        long currentUserId = authorizationService.getCurrentUser().id();
        long userId = activityRepository.getReferenceById(activity.id()).getUserId();
        if (currentUserId == userId) {
            String name = activity.name();
            double kcalPerMinute = activity.kcalPerMinute();

            if (!name.isEmpty()) {
                temp.setName(activity.name());
            }
            if (kcalPerMinute != 0) {
                temp.setKcalPerMinute(activity.kcalPerMinute());
            }

            var entity = activityRepository.save(temp);
            return toActivity(entity);
        } else throw unauthorized();
    }

    @Override
    public void deleteActivity(long id) {
        logger.debug("Deleting activity id: " + id);
        activityRepository.delete(activityRepository.getReferenceById(id));
    }

    private AuthorizationException unauthorized() {
        var authorizationException = new AuthorizationException("Activity is not authorized for this operation.");
        logger.error(authorizationException.getMessage());
        return authorizationException;
    }

    public static ActivityEntity fromActivity(long userId, NewActivity activity) {
        ActivityEntity entity = new ActivityEntity();
        entity.setUserId(userId);
        entity.setType("USER");
        entity.setName(activity.name());
        entity.setKcalPerMinute(activity.kcalPerMinute());
        return entity;
    }

    public static Activity toActivity(ActivityEntity entity) {
        return new Activity(entity.getId(), entity.getUserId(), entity.getType(), entity.getName(), entity.getKcalPerMinute());
    }

}
