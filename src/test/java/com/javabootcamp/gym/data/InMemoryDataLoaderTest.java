package com.javabootcamp.gym.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InMemoryDataLoaderTest {

//    private static SimpleDateFormat formatter;
    @Autowired
    private InMemoryDataLoader loader;

    @BeforeAll
    public static void load() {
//        formatter = new SimpleDateFormat("M/dd/yyy");
    }

    @Test
    void loadUsers() {

        // act
        var users = loader.loadUsers();

        // assert
        assertNotNull(users);
        assertEquals(1000, users.size());
    }

    @Test
    void loadTrainingType() {

        // act
        var types = loader.loadTrainingTypes();

        // assert
        assertNotNull(types);
        assertEquals(1000, types.size());
    }

    @Test
    void loadTrainees() {

        // act
        var trainees = loader.loadTrainees();

        // assert
        assertNotNull(trainees);
        assertEquals(1000, trainees.size());
    }

    @Test
    void loadTrainers() {

        // act
        var trainers = loader.loadTrainers();

        // assert
        assertNotNull(trainers);
        assertEquals(969, trainers.size());
    }

    @Test
    void loadTrainings() {

        // act
        var trainings = loader.loadTrainings();

        // assert
        assertNotNull(trainings);
        assertEquals(1000, trainings.size());
    }
}