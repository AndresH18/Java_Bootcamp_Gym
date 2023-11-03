package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.model.Trainee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TraineeDaoTest {
    private final TraineeDao dao;

    @Autowired
    public TraineeDaoTest(TraineeDao dao) {
        this.dao = dao;
    }

    @Test
    void create() {
        // arrange
        var t = new Trainee(1, new Date(), "Somewhere 123");

        // act
        var r = dao.create(t);

        // assert
        assertEquals(1, r.getId());
    }

    @Test
    void getById() {
        // act
        var t = dao.getById(1);

        // assert
        assertNotNull(t);
        assertEquals(1, t.getId());
        assertEquals("Somewhere 123", t.getAddress());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}