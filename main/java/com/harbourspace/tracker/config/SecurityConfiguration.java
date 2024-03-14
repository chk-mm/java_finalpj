package com.harbourspace.tracker.config;

import com.harbourspace.tracker.authorization.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Optional;

/**
 * No that this is not standard or common security configuration in Spring Boot. In real world, you would use
 * different authentication and authorization mechanisms, like JWT, OAuth, etc.
 * This is very simple and naive implementation, for the purpose of this exercise.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final AuthorizationService authorizationService;

    public SecurityConfiguration(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthorizationManager<RequestAuthorizationContext> authorizationManager
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // should not be disabled in real world application
                .authorizeHttpRequests(request -> request
                        .anyRequest()
                        .access(authorizationManager)
                )
                .build();
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> authorizationManager() {
        return (authentication, context) -> {
            var useId = getUserIdIfPresent(context);
            if (useId.isPresent()) {
                authorizationService.setSecurityContext(useId.get());
                logger.info("Authorization granted.");
                return new AuthorizationDecision(true);
            } else {
                logger.error("Authorization not granted.");
                return new AuthorizationDecision(false);
            }
        };
    }

    private Optional<Long> getUserIdIfPresent(RequestAuthorizationContext context) {
        var authHeader = context.getRequest().getHeader("Authorization");
        if (authHeader != null) {
            try {
                var userId = Long.parseLong(authHeader.replace("Basic ", ""));
                return Optional.of(userId);
            } catch (NumberFormatException e) {
                logger.error("Authorization header is invalid.");
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
