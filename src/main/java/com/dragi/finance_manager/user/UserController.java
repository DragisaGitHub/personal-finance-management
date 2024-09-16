package com.dragi.finance_manager.user;

import com.dragi.finance_manager.enums.Role;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/{username}/roles")
    public User addRolesToUser(@PathVariable String username, @RequestBody Set<Role> roles) {
        User user = userService.getUserByUsername(username);
        userService.assignRolesToUser(user, roles);
        return user;
    }
}