package com.harbourspace.tracker.activity.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityJpaRepository extends JpaRepository<ActivityEntity, Long> {

    List<ActivityEntity> findByUserId(Long userId);

    List<ActivityEntity> findByType(String type);
}
