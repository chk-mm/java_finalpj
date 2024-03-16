package com.harbourspace.tracker.activity;

import com.harbourspace.tracker.activity.model.Activity;
import com.harbourspace.tracker.activity.model.NewActivity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    ResponseEntity<List<Activity>> getActivityByUserId() {
        return ResponseEntity.ok(activityService.getActivityByUserId());
    }

    @GetMapping("{id}")
    ResponseEntity<Activity> getActivityByActivityId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(activityService.getActivityByActivityId(id));
    }

    @PostMapping("{userId}")
    ResponseEntity<Activity> createActivity(@PathVariable("userId") Long userId, @RequestBody NewActivity activity) {
        return new ResponseEntity<>(activityService.createActivity(userId, activity), HttpStatus.CREATED);
    }

    @PutMapping("{userId}")
    ResponseEntity<Activity> updateActivity(@PathVariable("userId") Long userId, @RequestBody Activity activity) {
        return ResponseEntity.ok(activityService.updateActivity(userId, activity.copyWithId(userId)));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Object> deleteActivity(@RequestParam("userId") Long userId, @PathVariable("id") Long id) {
        activityService.deleteActivity(userId, id);
        return ResponseEntity.ok().build();
    }
}
