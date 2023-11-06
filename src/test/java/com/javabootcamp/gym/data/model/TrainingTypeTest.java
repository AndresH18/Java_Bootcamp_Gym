package com.javabootcamp.gym.data.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainingTypeTypeTest {
    @Test
    void testTrainingTypeConstructorWithId() {
        TrainingType trainingType = new TrainingType(1, "Strength");

        assertEquals(1, trainingType.getId());
        assertEquals("Strength", trainingType.getName());

    }

    @Test
    void testTrainingTypeConstructorWithoutId() {
        TrainingType trainingType = new TrainingType( "Strength");

        assertEquals("Strength", trainingType.getName());

    }

    @Test
    void testTrainingTypeNotEquals() {
        TrainingType trainingType1 = new TrainingType(1, "Strength");
        TrainingType trainingType2 = new TrainingType(12, "Strength");

        assertNotEquals(trainingType1, trainingType2);
    }

    @Test
    void testTrainingTypeHashCode() {
        TrainingType trainingType1 = new TrainingType(1, "Strength");
        TrainingType trainingType2 = new TrainingType(2, "Strength");

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