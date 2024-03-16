package com.harbourspace.tracker.activity;

import com.harbourspace.tracker.activity.model.Activity;
import com.harbourspace.tracker.activity.model.NewActivity;

import java.util.List;

public interface ActivityService {

    List<Activity> getActivityByUserId();

    Activity getActivityByActivityId(long id);

    Activity createActivity(long userId, NewActivity activity);

    Activity updateActivity(long userId, Activity activity);

    void deleteActivity(long userId, long id);

}
