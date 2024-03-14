package com.harbourspace.tracker.user.jpa;

import com.harbourspace.tracker.authorization.AuthorizationService;
import com.harbourspace.tracker.error.AuthorizationException;
import com.harbourspace.tracker.user.UserFixtures;
import com.harbourspace.tracker.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.harbourspace.tracker.user.jpa.UserJpaService.fromUser;
import static org.mockito.ArgumentMatchers.any;

public class UserJpaServiceTest {

    private final UserJpaRepository userRepository = Mockito.mock(UserJpaRepository.class);
    private final AuthorizationService authorizationService = Mockito.mock(AuthorizationService.class);

    private final UserService userService = new UserJpaService(userRepository, authorizationService);

    @BeforeEach
    public void mock() {
        Mockito.when(authorizationService.isSystem()).thenReturn(true);
    }

    @Test
    @DisplayName("should return all users")
    public void testGetUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(UserFixtures.users.stream().map(UserJpaService::fromUser).toList());

        Assertions.assertEquals(UserFixtures.users, userService.getUsers());

        Mockito.verify(userRepository).findAll();
    }

    @Test
    @DisplayName("should user by id")
    public void testGetUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(fromUser(UserFixtures.user1)));

        Assertions.assertEquals(UserFixtures.user1, userService.getUserById(1L));

        Mockito.verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("should create user")
    public void testCreateUser() {
        var entity = fromUser(UserFixtures.user3);

        Mockito.when(userRepository.save(any())).thenReturn(entity);
        Mockito.when(userRepository.getReferenceById(any())).thenReturn(entity);

        Assertions.assertEquals(UserFixtures.user3, userService.createUser(UserFixtures.newUser));

        Mockito.verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("should update user")
    public void testUpdateUser() {
        var entity = fromUser(UserFixtures.user3Updated);

        Mockito.when(userRepository.save(any())).thenReturn(entity);
        Mockito.when(userRepository.getReferenceById(any())).thenReturn(entity);

        Assertions.assertEquals(UserFixtures.user3Updated, userService.updateUser(UserFixtures.user3Updated));

        Mockito.verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("should delete user")
    public void testDeleteUser() {
        var entity = fromUser(UserFixtures.user3);
        Mockito.when(userRepository.getReferenceById(entity.getId())).thenReturn(entity);

        userService.deleteUser(entity.getId());

        Mockito.verify(userRepository).delete(entity);
    }

    @Test
    @DisplayName("get users should throw exception when not admin")
    public void testGetUsersNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, userService::getUsers);

        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("get user should throw exception when not admin")
    public void getGetUserNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, () -> userService.getUserById(1L));

        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("create user should throw exception when not admin")
    public void createUserNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, () -> userService.createUser(UserFixtures.newUser));

        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("update user should throw exception when not admin")
    public void testUpdateUserNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, () -> userService.updateUser(UserFixtures.user3Updated));

        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("delete user should throw exception when not admin")
    public void testDeleteUserNotAdmin() {
        Mockito.when(authorizationService.isSystem()).thenReturn(false);

        Assertions.assertThrows(AuthorizationException.class, () -> userService.deleteUser(3L));

        Mockito.verifyNoInteractions(userRepository);
    }
}
