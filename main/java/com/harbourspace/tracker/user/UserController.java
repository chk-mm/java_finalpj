package com.harbourspace.tracker.user;

import com.harbourspace.tracker.user.model.NewUser;
import com.harbourspace.tracker.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<List<User>>getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("{id}")
    ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    ResponseEntity<User> createUser(@RequestBody NewUser user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    ResponseEntity<User> updateUser(
            @PathVariable("id") Long id,
            @RequestBody User user
    ) {
        return ResponseEntity.ok(userService.updateUser(user.copyWithId(id)));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
