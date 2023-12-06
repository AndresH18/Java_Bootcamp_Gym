package com.javabootcamp.gym.data;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static com.javabootcamp.gym.data.IDataSource.MemoryModels.*;

class TraineeTest {

    @Test
    void testTraineeConstructorWithId() {
        Trainee trainee = new Trainee(1, 1001, LocalDate.now(), "123 Main St");

        assertEquals(1, trainee.getId());
        assertEquals(1001, trainee.getUserId());
        assertNotNull(trainee.getDateOfBirth());
        assertEquals("123 Main St", trainee.getAddress());
    }

    @Test
    void testTraineeConstructorWithoutId() {
        Trainee trainee = new Trainee(1001, LocalDate.now(), "123 Main St");

        assertEquals(1001, trainee.getUserId());
        assertNotNull(trainee.getDateOfBirth());
        assertEquals("123 Main St", trainee.getAddress());
    }

    @Test
    void testTraineeNotEquals() {
        Trainee trainee1 = new Trainee(1, 1001, LocalDate.now(), "123 Main St");
        Trainee trainee2 = new Trainee(2, 1002, LocalDate.now(), "456 Elm St");

        assertNotEquals(trainee1, trainee2);
    }

    @Test
    void testTraineeHashCode() {
        Trainee trainee1 = new Trainee(1, 1001, LocalDate.now(), "123 Main St");
        Trainee trainee2 = new Trainee(2, 1002, LocalDate.now(), "456 Elm St");


        assertNotEquals(trainee1.hashCode(), trainee2.hashCode());
    }

    @Test
    void testTraineeGettersSetters() {
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.minusYears(1);
        Trainee trainee = new Trainee(1, 2, date1, "House");

        trainee.setId(2);
        trainee.setUserId(3);
        trainee.setDateOfBirth(date2);
        trainee.setAddress("Apartment");


        assertNotEquals(date1, trainee.getDateOfBirth());
        assertNotEquals(1, trainee.getId());
        assertNotEquals(2, trainee.getUserId());
        assertNotEquals("House", trainee.getAddress());

        assertEquals(2, trainee.getId());
        assertEquals(3, trainee.getUserId());
        assertEquals(date2, trainee.getDateOfBirth());
        assertEquals("Apartment", trainee.getAddress());
    }
}

