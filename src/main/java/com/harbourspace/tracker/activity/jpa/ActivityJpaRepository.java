package com.harbourspace.tracker.activity.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityJpaRepository extends JpaRepository<ActivityEntity, Long> {

    Optional<ActivityEntity> findByName(String name);

}
