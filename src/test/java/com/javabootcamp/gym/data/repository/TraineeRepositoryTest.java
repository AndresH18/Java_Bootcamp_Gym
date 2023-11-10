package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.Trainee;
import com.javabootcamp.gym.data.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TraineeRepositoryTest {

    @Autowired
    private TraineeRepository repository;


    @Test
    void test() {
        var user = new User(1, "Andres", "Hoyos", " andres.hoyos", "00andres00", false);
        var trainee = new Trainee();

        trainee.setAddress("Colombia");
        trainee.setDateOfBirth(LocalDate.now());

        trainee.setUser(user);

        repository.save(trainee);
    }

}