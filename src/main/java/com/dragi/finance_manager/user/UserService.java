package com.dragi.finance_manager.user;

import com.dragi.finance_manager.enums.Role;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Collections.singleton(Role.USER));
        }
        return userRepository.save(user);
    }

    public void assignRolesToUser(User user, Set<Role> roles) {
        user.getRoles().addAll(roles);
        userRepository.save(user);
    }
}