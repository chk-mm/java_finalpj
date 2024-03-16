package com.harbourspace.tracker.exercise.jpa;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "exercise")
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long activityId;
    private Timestamp startTime;
    private Long duration;
    private double kcalBurned;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public double getKcalBurned() {
        return kcalBurned;
    }

    public void setKcalBurned(double kcalBurned) {
        this.kcalBurned = kcalBurned;
    }
}
