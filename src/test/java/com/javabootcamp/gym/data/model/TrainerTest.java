package com.javabootcamp.gym.data.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainerTest {
    @Test
    void testTrainerConstructorWithId() {
        Trainer trainer = new Trainer(1, 2, 3);

        assertEquals(1, trainer.getId());
        assertEquals(2, trainer.getUserId());
        assertEquals(3, trainer.getSpecializationId());
    }

    @Test
    void testTrainerConstructorWithoutId() {
        Trainer trainer = new Trainer(2, 3);

        assertEquals(2, trainer.getUserId());
        assertEquals(3, trainer.getSpecializationId());
    }

    @Test
    void testTrainerNotEquals() {
        Trainer trainer1 = new Trainer(1, 2, 3);
        Trainer trainer2 = new Trainer(2, 3, 3);

        assertNotEquals(trainer1, trainer2);
    }

    @Test
    void testTrainerHashCode() {
        Trainer trainer1 = new Trainer(1, 2, 3);
        Trainer trainer2 = new Trainer(2, 3, 4);


        assertNotEquals(trainer1.hashCode(), trainer2.hashCode());
    }

    @Test
    void testTrainerGettersSetters() {

        Trainer trainer = new Trainer(1, 2, 3);

        trainer.setId(2);
        trainer.setUserId(3);
        trainer.setSpecializationId(4);


        assertNotEquals(1, trainer.getId());
        assertNotEquals(2, trainer.getUserId());
        assertNotEquals(3, trainer.getSpecializationId());

        assertEquals(2, trainer.getId());
        assertEquals(3, trainer.getUserId());
        assertEquals(4, trainer.getSpecializationId());
    }
}