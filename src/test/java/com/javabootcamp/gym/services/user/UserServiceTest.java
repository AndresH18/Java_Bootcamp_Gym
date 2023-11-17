package com.javabootcamp.gym.services.user;

import com.javabootcamp.gym.data.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Test
    public void authenticate() {
        // arrange
        var repo = mock(UserRepository.class);
        when(repo.existsUserByUsernameAndPassword(anyString(), anyString())).thenReturn(true);
        var service = new UserService(repo);

        // act
        var r = service.authenticate("Hello", "World");

        // assert
        assertTrue(r);
    }

    @Test
    public void createUser_returnsNotNul() {
        var repo = mock(UserRepository.class);

    }

}