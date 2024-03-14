package com.harbourspace.tracker.user.jpa;

import com.harbourspace.tracker.user.UserFixtures;
import com.harbourspace.tracker.user.jdbc.UserJdbcRepository;
import com.harbourspace.tracker.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import static com.harbourspace.tracker.user.jpa.UserJpaService.fromUser;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userRepository;

    @Test
    @DisplayName("should select all users")
    public void testSelectAll() {
        var users = userRepository.findAll();

        for (int i = 0; i < UserFixtures.users.size(); i++) {
            var expected = UserFixtures.users.get(i);
            var actual = users.get(i);
            Assertions.assertEquals(expected.id(), actual.getId());
            Assertions.assertEquals(expected.name(), actual.getName());
        }
    }

    @Test
    @DisplayName("should select user by id")
    public void testSelectUserById() {
        var expected = UserFixtures.user1;
        var actual = userRepository.findById(1L).orElseThrow();
        Assertions.assertEquals(expected.id(), actual.getId());
        Assertions.assertEquals(expected.name(), actual.getName());
    }

    @Test
    @DisplayName("should select user by name")
    public void testSelectUserByName() {
        var expected = UserFixtures.user2;
        var actual = userRepository.findByName(expected.name()).orElseThrow();
        Assertions.assertEquals(expected.id(), actual.getId());
        Assertions.assertEquals(expected.name(), actual.getName());
    }

    @Test
    @DisplayName("should insert user")
    public void testInsertUser() {
        Assertions.assertInstanceOf(UserEntity.class, userRepository.save(fromUser(UserFixtures.newUser)));
    }

    @Test
    @DisplayName("should update user")
    public void testUpdateUser() {

        var user = userRepository.save(fromUser(UserFixtures.newUser));
        user.setName(user.getName() + " UPDATED");

        var updatedUser = userRepository.save(user);

        Assertions.assertEquals(user, updatedUser);
    }

    @Test
    @DisplayName("should delete user")
    public void testDeleteUser() {
        var user = userRepository.save(fromUser(UserFixtures.newUser));
        userRepository.delete(user);
        Assertions.assertFalse(userRepository.findById(user.getId()).isPresent());

    }
}
