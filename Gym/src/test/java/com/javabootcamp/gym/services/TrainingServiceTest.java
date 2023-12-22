package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.dto.TrainingDto;
import com.javabootcamp.gym.data.model.Trainee;
import com.javabootcamp.gym.data.model.Trainer;
import com.javabootcamp.gym.data.model.Training;
import com.javabootcamp.gym.data.model.TrainingType;
import com.javabootcamp.gym.services.delegate.repository.TraineeRepositoryDelegate;
import com.javabootcamp.gym.services.delegate.repository.TrainerRepositoryDelegate;
import com.javabootcamp.gym.services.delegate.repository.TrainingRepositoryDelegate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    private static TrainingRepositoryDelegate trainingRepository;
    private static TraineeRepositoryDelegate traineeRepository;
    private static TrainerRepositoryDelegate trainerRepository;

    private TrainingService service;

    @BeforeEach
    public void beforeEach() {
        trainingRepository = mock(TrainingRepositoryDelegate.class);
        traineeRepository = mock(TraineeRepositoryDelegate.class);
        trainerRepository = mock(TrainerRepositoryDelegate.class);

        service = new TrainingService(trainingRepository, traineeRepository, trainerRepository, null);
    }

    @Test
    void create_exceptionThrown_returnsNull() {
        when(traineeRepository.findByUsername(anyString())).thenThrow(new RuntimeException());

        var t = service.create(new TrainingDto("trainee", "trainer", "training name", LocalDate.now(), 10));

        assertNull(t);
    }

    @Test
    void create_traineeNotFound_returnNull() {
        when(traineeRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());
        when(trainerRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(new Trainer()));

        var t = service.create(new TrainingDto("trainee", "trainer", "training name", LocalDate.now(), 10));

        assertNull(t);
    }

    @Test
    void create_trainerNotFound_returnNull() {
        when(traineeRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(new Trainee()));
        when(trainerRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        var t = service.create(new TrainingDto("trainee", "trainer", "training name", LocalDate.now(), 10));

        assertNull(t);
    }

    @Test
    void create_returnsNotNull() {
        var trainer = new Trainer(1, TrainingType.RESISTANCE, null);
        var trainee = new Trainee(2, LocalDate.now(), "House");
        var dto = new TrainingDto("trainee-username", "trainer-username", "training-name", LocalDate.now(), 50);

        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(trainer));
        when(trainingRepository.save(any())).thenAnswer(a -> {
            Training training = a.getArgument(0);
            training.setId(333);
            return training;
        });

        var t = service.create(dto);

        assertNotNull(t);
        assertEquals(trainer, t.getTrainer());
        assertEquals(trainee, t.getTrainee());
        assertEquals(trainer.getSpecialization(), t.getTrainingType());
    }

}