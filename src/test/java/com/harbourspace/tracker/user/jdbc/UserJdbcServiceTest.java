package com.harbourspace.tracker.user.jdbc;

import com.harbourspace.tracker.error.AuthorizationException;
import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.user.UserFixtures;
import com.harbourspace.tracker.user.jdbc.UserJdbcRepository;
import com.harbourspace.tracker.user.jdbc.UserJdbcService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserJdbcServiceTest {

    private final UserJdbcRepository userRepository = Mockito.mock(UserJdbcRepository.class);
    private final AuthorizationService authorizationService = Mockito.mock(AuthorizationService.class);

    private final UserJdbcService userServiceImpl = new UserJdbcService(userRepository, authorizationService);

    @BeforeEach
    public void mock() {
        Mockito.when(authorizationService.isSystem()).thenReturn(true);
        Mockito.when(userRepository.selectAll()).thenReturn(UserFixtures.users);
        Mockito.when(userRepository.selectById(1L)).thenReturn(UserFixtures.user1);
    }

    @Test
    @DisplayName("should return all users")
    public void testGetUsers() {
        Assertions.assertEquals(UserFixtures.users, userServiceImpl.getUsers());

        Mockito.verify(userRepository).selectAll();
    }

    @Test
    @DisplayName("should user by id")
    public void testGetUser() {
        Assertions.assertEquals(UserFixtures.user1, userServiceImpl.getUserById(1L));

        Mockito.verify(userRepository).selectById(1L);
    }

    @Test
    @DisplayName("should create user")
    public void testCreateUser() {

        Mockito.when(userRepository.insert(UserFixtures.newUser)).thenReturn(UserFixtures.user3);

        Assertions.assertEquals(UserFixtures.user3, userServiceImpl.createUser(UserFixtures.newUser));

        Mockito.verify(userRepository).insert(UserFixtures.newUser);
    }

    @Test
    @DisplayName("should update user")
    public void testUpdateUser() {
        Mockito.when(userRepository.update(UserFixtures.user3Updated)).thenReturn(UserFixtures.user3Updated);

        Assertions.assertEquals(UserFixtures.user3Updated, userServiceImpl.updateUser(UserFixtures.user3Updated));

        Mockito.verify(userRepository).update(UserFixtures.user3Updated);
    }

    @Test
    @DisplayName("should delete user")
    public void testDeleteUser() {
        userServiceImpl.deleteUser(3L);

        Mockito.verify(userRepository).delete(3L);
    }

    @Test
    @DisplayName("get users should throw exception when not admin")
    public void testGetUsersNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, userServiceImpl::getUsers);

        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("get user should throw exception when not admin")
    public void getGetUserNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, () -> userServiceImpl.getUserById(1L));

        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("create user should throw exception when not admin")
    public void createUserNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, () -> userServiceImpl.createUser(UserFixtures.newUser));

        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("update user should throw exception when not admin")
    public void testUpdateUserNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, () -> userServiceImpl.updateUser(UserFixtures.user3Updated));

        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("delete user should throw exception when not admin")
    public void testDeleteUserNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, () -> userServiceImpl.deleteUser(3L));

        Mockito.verifyNoInteractions(userRepository);
    }
}
