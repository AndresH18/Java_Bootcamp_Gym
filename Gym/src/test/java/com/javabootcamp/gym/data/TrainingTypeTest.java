package com.javabootcamp.gym.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static com.javabootcamp.gym.data.IDataSource.MemoryModels.*;

class TrainingTypeTest {
    @Test
    void testTrainingTypeConstructorWithId() {
        com.javabootcamp.gym.data.model.TrainingType trainingType = new com.javabootcamp.gym.data.model.TrainingType(1, "Strength");

        assertEquals(1, trainingType.getId());
        assertEquals("Strength", trainingType.getName());

    }

    @Test
    void testTrainingTypeConstructorWithoutId() {
        com.javabootcamp.gym.data.model.TrainingType trainingType = new com.javabootcamp.gym.data.model.TrainingType("Strength");

        assertEquals("Strength", trainingType.getName());

    }

    @Test
    void testTrainingTypeNotEquals() {
        com.javabootcamp.gym.data.model.TrainingType trainingType1 = new com.javabootcamp.gym.data.model.TrainingType(1, "Strength");
        com.javabootcamp.gym.data.model.TrainingType trainingType2 = new com.javabootcamp.gym.data.model.TrainingType(12, "Strength");

        assertNotEquals(trainingType1, trainingType2);
    }

    @Test
    void testTrainingTypeHashCode() {
        com.javabootcamp.gym.data.model.TrainingType trainingType1 = new com.javabootcamp.gym.data.model.TrainingType(1, "Strength");
        com.javabootcamp.gym.data.model.TrainingType trainingType2 = new com.javabootcamp.gym.data.model.TrainingType(2, "Strength");

        assertNotEquals(trainingType1.hashCode(), trainingType2.hashCode());
    }

    @Test
    void testTrainingTypeGettersSetters() {
        TrainingType trainingType = new TrainingType(1, "Strength");

        trainingType.setId(2);
        trainingType.setName("Gorilla-Strength");


        assertNotEquals(1, trainingType.getId());
        assertNotEquals("Strength", trainingType.getName());

        assertEquals(2, trainingType.getId());
        assertEquals("Gorilla-Strength", trainingType.getName());
    }
}