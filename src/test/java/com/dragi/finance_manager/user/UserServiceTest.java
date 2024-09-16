package com.dragi.finance_manager.user;

import com.dragi.finance_manager.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setRoles(Collections.singleton(Role.USER));
    }

    @Test
    void shouldCreateUserWithDefaultRole() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("john_doe", createdUser.getUsername());
        assertTrue(createdUser.getRoles().contains(Role.USER));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldFindUserByUsername() {
        when(userRepository.findByUsername("john_doe")).thenReturn(user);

        User foundUser = userService.getUserByUsername("john_doe");

        assertNotNull(foundUser);
        assertEquals("john_doe", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("john_doe");
    }
}