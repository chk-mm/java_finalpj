package com.harbourspace.tracker.user.jdbc;

import com.harbourspace.tracker.error.AuthorizationException;
import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.user.UserService;
import com.harbourspace.tracker.user.model.NewUser;
import com.harbourspace.tracker.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The UserJdbcService uses the UserJdbcRepository to perform CRUD operations on users.
 */
//@Primary
@Service
public class UserJdbcService implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserJdbcService.class);

    private final UserJdbcRepository userRepository;
    private final AuthorizationService authorizationService;

    public UserJdbcService(UserJdbcRepository userRepository, AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public List<User> getUsers() {
        if (authorizationService.isSystem()) {
            logger.debug("Getting all users");
            return userRepository.selectAll();
        } else throw unauthorized();
    }

    @Override
    public User getUserById(long id) {
        if (authorizationService.isSystem()) {
            logger.debug("Getting user " + id);
            return userRepository.selectById(id);
        } else throw unauthorized();
    }

    @Override
    public User getUserByName(String name) {
        if (authorizationService.isSystem()) {
            logger.debug("Getting user " + name);
            return userRepository.selectByName(name);
        } else throw unauthorized();
    }

    @Override
    public User createUser(NewUser user) {
        if (authorizationService.isSystem()) {
            logger.debug("Creating new user: " + user);
            return userRepository.insert(user);
        } else throw unauthorized();
    }


    @Override
    public User updateUser(User user) {
        if (authorizationService.isSystem()) {
            logger.debug("Updating user: "+ user);
            return userRepository.update(user);
        } else throw unauthorized();
    }

    @Override
    public void deleteUser(long id) {
        if (authorizationService.isSystem()) {
            logger.debug("Deleting user " + id);
            userRepository.delete(id);
        } else throw unauthorized();
    }

    private AuthorizationException unauthorized() {
        var authorizationException = new AuthorizationException("User is not authorized for this operation.");
        logger.error(authorizationException.getMessage());
        return authorizationException;
    }
}
