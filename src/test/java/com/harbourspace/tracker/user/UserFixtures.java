package com.harbourspace.tracker.user;

import com.harbourspace.tracker.user.model.NewUser;
import com.harbourspace.tracker.user.model.User;

import java.util.List;

public class UserFixtures {

    public static final User user0 = new User(0L, "SYSTEM");
    public static final User user1 = new User(1L, "John");
    public static final User user2 = new User(2L, "Jane");
    public static final List<User> users = List.of(user0, user1, user2);
    public static final NewUser newUser = new NewUser("Jack");
    public static final User user3 = new User(3L, newUser.name());
    public static  final User user3Updated = new User(user3.id(), user3.name() + " UPDATED");

}
