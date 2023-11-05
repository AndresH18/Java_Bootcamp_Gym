package com.javabootcamp.gym.data.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainingTest {
    @Test
    void testTrainingConstructorWithId() {
        LocalDate date = LocalDate.now();
        Training training = new Training(1, 2, 3, 4, "Weight", date, 3);

        assertEquals(1, training.getId());
        assertEquals(2, training.getTraineeId());
        assertEquals(3, training.getTrainerId());
        assertEquals(4, training.getTrainingTypeId());
        assertEquals("Weight", training.getName());
        assertEquals(date, training.getDate());
        assertEquals(3, training.getDuration());
    }

    @Test
    void testTrainingConstructorWithoutId() {
        LocalDate date = LocalDate.now();
        Training training = new Training(2, 3, 4, "Weight", date, 3);

        assertEquals(2, training.getTraineeId());
        assertEquals(3, training.getTrainerId());
        assertEquals(4, training.getTrainingTypeId());
        assertEquals("Weight", training.getName());
        assertEquals(date, training.getDate());
        assertEquals(3, training.getDuration());
    }

    @Test
    void testTrainingNotEquals() {
        LocalDate date = LocalDate.now();
        Training training1 = new Training(1, 2, 3, 4, "Weight", date, 3);
        Training training2 = new Training(2, 3, 4, 5, "Running", date.plusYears(1), 4);

        assertNotEquals(training1, training2);
    }

    @Test
    void testTrainingHashCode() {
        LocalDate date = LocalDate.now();

        Training training1 = new Training(1, 2, 3, 4, "Weight", date, 3);
        Training training2 = new Training(2, 3, 4, 4, "Weight", date, 3);

        assertNotEquals(training1.hashCode(), training2.hashCode());
    }

    @Test
    void testTrainingGettersSetters() {
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.minusYears(1);
        Training training = new Training(1, 2, 3, 4, "Weight", date1, 3);

        training.setId(2);
        training.setTraineeId(3);
        training.setTrainerId(4);
        training.setTrainingTypeId(5);
        training.setName("Cheetah-Running");
        training.setDate(date2);
        training.setDuration(10);


        assertNotEquals(1, training.getId());
        assertNotEquals(2, training.getTraineeId());
        assertNotEquals(3, training.getTrainerId());
        assertNotEquals(4, training.getTrainingTypeId());
        assertNotEquals("Weight", training.getName());
        assertNotEquals(date1, training.getDate());
        assertNotEquals(3, training.getDuration());

        assertEquals(2, training.getId());
        assertEquals(3, training.getTraineeId());
        assertEquals(4, training.getTrainerId());
        assertEquals(5, training.getTrainingTypeId());
        assertEquals("Cheetah-Running", training.getName());
        assertEquals(date2, training.getDate());
        assertEquals(10, training.getDuration());
    }
}