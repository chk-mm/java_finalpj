package com.harbourspace.tracker.activity.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "activity")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String type;
    private String name;
    private double kcalPerMinute;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKcalPerMinute() {
        return kcalPerMinute;
    }

    public void setKcalPerMinute(double kcalPerMinute) {
        this.kcalPerMinute = kcalPerMinute;
    }
}
