package com.harbourspace.tracker.user.jpa;

import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.error.AuthorizationException;
import com.harbourspace.tracker.error.NotFoundException;
import com.harbourspace.tracker.user.UserService;
import com.harbourspace.tracker.user.model.NewUser;
import com.harbourspace.tracker.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The UserJpaService uses the UserJpaRepository to perform CRUD operations on users.
 */
@Primary
@Service
public class UserJpaService implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserJpaService.class);

    private final UserJpaRepository userRepository;
    private final AuthorizationService authorizationService;

    public UserJpaService(UserJpaRepository userRepository, AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public List<User> getUsers() {
        if (authorizationService.isSystem()) {
            logger.debug("Getting all users");
            return userRepository.findAll().stream().map(UserJpaService::toUser).toList();
        } else throw unauthorized();
    }

    @Override
    public User getUserById(long id) {
        if (authorizationService.isSystem()) {
            logger.debug("Getting user " + id);
            var entity =  userRepository.findById(id).orElseThrow(() ->
                    new NotFoundException("User " + id + " not found")
            );
            return toUser(entity);
        } else throw unauthorized();
    }

    @Override
    public User getUserByName(String name) {
        if (authorizationService.isSystem()) {
            logger.debug("Getting user " + name);
            var entity =  userRepository.findByName(name).orElseThrow(() ->
                    new NotFoundException("User " + name + " not found")
            );
            return toUser(entity);
        } else throw unauthorized();
    }

    @Override
    public User createUser(NewUser user) {
        if (authorizationService.isSystem()) {
            logger.debug("Creating new user: " + user);
            var entity = userRepository.save(fromUser(user));
            return toUser(entity);
        } else throw unauthorized();
    }

    @Override
    public User updateUser(User user) {
        if (authorizationService.isSystem()) {
            logger.debug("Updating user: " + user);
            var entity = userRepository.save(fromUser(user));
            return toUser(entity);
        } else throw unauthorized();
    }

    @Override
    public void deleteUser(long id) {
        if (authorizationService.isSystem()) {
            logger.debug("Deleting user " + id);
            userRepository.delete(userRepository.getReferenceById(id));
        } else throw unauthorized();
    }

    private AuthorizationException unauthorized() {
        var authorizationException = new AuthorizationException("User is not authorized for this operation.");
        logger.error(authorizationException.getMessage());
        return authorizationException;
    }

    public static UserEntity fromUser(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setName(user.name());
        return entity;
    }

    public static UserEntity fromUser(NewUser user) {
        UserEntity entity = new UserEntity();
        entity.setName(user.name());
        return entity;
    }

    public static User toUser(UserEntity entity) {
        return new User(entity.getId(), entity.getName());
    }
}
