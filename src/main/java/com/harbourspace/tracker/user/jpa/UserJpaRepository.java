package com.harbourspace.tracker.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This is an example of repository implementation using Spring Data JPA
 */
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name);

}

