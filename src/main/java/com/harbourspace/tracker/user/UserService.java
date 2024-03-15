package com.harbourspace.tracker.user;

import com.harbourspace.tracker.user.model.NewUser;
import com.harbourspace.tracker.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUserById(long id);

    User getUserByName(String name);

    User createUser(NewUser user);

    User updateUser(User user);

    void deleteUser(long id);
}
