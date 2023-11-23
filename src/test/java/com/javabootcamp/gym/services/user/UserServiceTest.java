package com.javabootcamp.gym.services.user;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private static PasswordEncoder passwordEncoder;
    private static UserRepository userRepository;

    private UserService service;

    @BeforeAll
    public static void beforeAll() {
        passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(anyString())).thenAnswer(a -> a.getArgument(0));
    }

    @BeforeEach
    public void beforeEach() {
        userRepository = mock(UserRepository.class);

        service = new UserService(userRepository, passwordEncoder);
    }


    @Test

    public void authenticate() {
        // arrange
        var repo = mock(UserRepository.class);
        when(repo.existsUserByUsernameAndPassword(anyString(), anyString())).thenReturn(true);
        var service = new UserService(repo, passwordEncoder);

        // act
        var r = service.authenticate("Hello", "World");

        // assert
        assertTrue(r);
    }

    @Test
    public void createUser_returnsNotNul() {
        long countReturn = 10L;
        String firstname = "Andres";
        String lastname = "Hoyos";
        String username = (firstname + "." + lastname + countReturn).toLowerCase();
        User.Role role = User.Role.TRAINEE;
        // arrange
        var repo = mock(UserRepository.class);
        when(repo.countUserByUsernameStartingWith(anyString())).thenReturn(countReturn);
        when(repo.save(any(User.class))).thenAnswer(a -> {
            User user = a.getArgument(0);
            user.setId(123);
            return user;
        });

        var service = new UserService(repo, passwordEncoder);

        // act
        var u = service.createUser(firstname, lastname, role);

        // assert
        assertNotNull(u);
        assertEquals(firstname, u.getFirstName());
        assertEquals(lastname, u.getLastName());
        assertEquals(role, u.getRole());
        assertEquals(123, u.getId());
        assertEquals(username, u.getUsername());
    }

    @Test
    public void createUser_returnsNul() {
        // arrange
        var repo = mock(UserRepository.class);
        when(repo.countUserByUsernameStartingWith(anyString())).thenThrow(new RuntimeException());

        UserService service = new UserService(repo, passwordEncoder);

        // act
        var u = service.createUser("Andres", "Hoyos", User.Role.TRAINEE);

        // assert
        assertNull(u);
    }

    @Test
    void changePassword_userNotFound_returnsFalse() {
        // arrange
        when(userRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.empty());
        var service = new UserService(userRepository, passwordEncoder);

        // act
        var r = service.changePassword("username", "oldpassword", "newpassword");

        // assert
        assertFalse(r);
    }

    @Test
    void changePassword_failedSave_returnsFalse() {
        int id = 123;
        String firstname = "Andres";
        String lastname = "Hoyos";
        String username = (firstname + "." + lastname).toLowerCase();
        String password = "1234567890";
        User.Role role = User.Role.TRAINEE;

        var user = new User(id, firstname, lastname, username, password, true);
        user.setRole(role);

        // arrange
        when(userRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenThrow(new RuntimeException());

        var service = new UserService(userRepository, passwordEncoder);

        // act
        var r = service.changePassword(username, password, "newPassword");

        // assert
        assertFalse(r);
    }

    @Test
    void changePassword_returnsTrue() {
        int id = 123;
        String firstname = "Andres";
        String lastname = "Hoyos";
        String username = (firstname + "." + lastname).toLowerCase();
        String password = "1234567890";
        String newPassword = "newPassword";
        User.Role role = User.Role.TRAINEE;

        var user = new User(id, firstname, lastname, username, password, true);
        user.setRole(role);

        // arrange
        when(userRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(any());

        var service = new UserService(userRepository, passwordEncoder);

        // act
        var r = service.changePassword(username, password, newPassword);

        // assert
        assertTrue(r);
        verify(userRepository).save(any());
    }

    @Test
    void get_returnsOptional() {
        // arrange
        when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        // act
        var optional = service.get("SomeString");

        // assert
        assertNotNull(optional);
        assertNotNull(optional);
    }

    @Test
    void setIsActive_userNotFound_returnsEmptyOptional() {
        // arrange
        when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        // act
        var optional = service.setIsActive("username", false);

        // assert
        assertNotNull(optional);
        assertTrue(optional.isEmpty());
    }

    @Test
    void setIsActive_returnsOptionalFalse() {
        // arrange
        when(userRepository.findByUsernameIgnoreCase(anyString())).thenThrow(new RuntimeException());

        // act
        var optional = service.setIsActive("username", false);

        // assert
        assertNotNull(optional);
        assertFalse(optional.isEmpty());
        assertFalse(optional.get());
    }

    @Test
    void setIsActive_returnsTrue() {
        // arrange
        var user = new User();
        user.setActive(false);
        when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(user));

        // act
        var optional = service.setIsActive("username", true);
        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertTrue(optional.get());

        assertTrue(user.isActive());
    }
}