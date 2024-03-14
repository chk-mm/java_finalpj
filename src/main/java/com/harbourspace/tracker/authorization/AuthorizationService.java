package com.harbourspace.tracker.authorization;

import com.harbourspace.tracker.config.ApplicationConfiguration;
import com.harbourspace.tracker.error.NotFoundException;
import com.harbourspace.tracker.user.model.User;
import com.harbourspace.tracker.user.jdbc.UserJdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * AuthorizationService is responsible for validating authorization context.
 */
@Service
public class AuthorizationService {

    private final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final UserJdbcRepository userRepository;

    public AuthorizationService(UserJdbcRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setSecurityContext(long userId) {
        Authentication auth = new UsernamePasswordAuthenticationToken(userId, "N/A");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    /**
     * Resolves current user context.
     * @return user as User
     */
    public User getCurrentUser() {
        var userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.selectById(userId);

        if (user == null) {
            logger.error("User not found for id: " + userId);
            throw new NotFoundException("User not found");
        } else {
            return user;
        }
    }

    /**
     * Returns true if current user is SYSTEM user
     * @return boolean
     */
    public boolean isSystem() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                .equals(ApplicationConfiguration.SYSTEM_USER_ID);
    }
}
