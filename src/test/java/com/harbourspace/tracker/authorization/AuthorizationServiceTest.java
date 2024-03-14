package com.harbourspace.tracker.authorization;

import com.harbourspace.tracker.user.jdbc.UserJdbcRepository;
import com.harbourspace.tracker.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AuthorizationServiceTest {

    @Autowired
    private AuthorizationService authorizationService;

    @MockBean
    private UserJdbcRepository userRepository;

    private final User system = new User(0L, "SYSTEM");
    private final User user = new User(123L, "User");

    @BeforeEach
    public void mock(){
        Mockito.when(userRepository.selectById(system.id())).thenReturn(system);
        Mockito.when(userRepository.selectById(user.id())).thenReturn(user);
    }

    @Test
    @DisplayName("should get current user context")
    public void testCurrentUserContext() {
        setSecurityContext(user.id());
        Assertions.assertEquals(user, authorizationService.getCurrentUser());
    }

    @Test
    @DisplayName("should test if user is SYSTEM: true")
    public void testIsSystemTrue() {
        setSecurityContext(system.id());
        Assertions.assertTrue(authorizationService.isSystem());
    }

    @Test
    @DisplayName("should test if user is SYSTEM: false")
    public void testIsSystemFalse() {
        setSecurityContext(user.id());
        Assertions.assertFalse(authorizationService.isSystem());
    }

    private void setSecurityContext(long userId) {
        authorizationService.setSecurityContext(userId);
    }
}
