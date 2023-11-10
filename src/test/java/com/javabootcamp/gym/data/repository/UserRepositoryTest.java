package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void test() {
//        var user = new User();
//
//        user.setFirstName("Andres");
//        user.setLastName("Hoyos");
//        user.setUsername("andres.hoyos");
//        user.setPassword("00andres00");
//        userRepository.save(user);
//
//    }
//}