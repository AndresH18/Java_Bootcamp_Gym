package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.dto.TrainingDto;
import com.javabootcamp.gym.data.model.Trainee;
import com.javabootcamp.gym.data.model.Trainer;
import com.javabootcamp.gym.data.model.Training;
import com.javabootcamp.gym.data.model.TrainingType;
import com.javabootcamp.gym.data.repository.TraineeRepository;
import com.javabootcamp.gym.data.repository.TrainerRepository;
import com.javabootcamp.gym.data.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    private static TrainingRepository trainingRepository;
    private static TraineeRepository traineeRepository;
    private static TrainerRepository trainerRepository;

    private TrainingService service;

    @BeforeEach
    public void beforeEach() {
        trainingRepository = mock(TrainingRepository.class);
        traineeRepository = mock(TraineeRepository.class);
        trainerRepository = mock(TrainerRepository.class);

        service = new TrainingService(trainingRepository, traineeRepository, trainerRepository, null);
    }

    @Test
    void create_exceptionThrown_returnsNull() {
        when(traineeRepository.findFirstByUserUsername(anyString())).thenThrow(new RuntimeException());

        var t = service.create(new TrainingDto("trainee", "trainer", "training name", LocalDate.now(), 10));

        assertNull(t);
    }

    @Test
    void create_traineeNotFound_returnNull() {
        when(traineeRepository.findFirstByUserUsername(anyString()))
                .thenReturn(Optional.empty());
        when(trainerRepository.findFirstByUserUsername(anyString()))
                .thenReturn(Optional.of(new Trainer()));

        var t = service.create(new TrainingDto("trainee", "trainer", "training name", LocalDate.now(), 10));

        assertNull(t);
    }

    @Test
    void create_trainerNotFound_returnNull() {
        when(traineeRepository.findFirstByUserUsername(anyString()))
                .thenReturn(Optional.of(new Trainee()));
        when(trainerRepository.findFirstByUserUsername(anyString()))
                .thenReturn(Optional.empty());

        var t = service.create(new TrainingDto("trainee", "trainer", "training name", LocalDate.now(), 10));

        assertNull(t);
    }

    @Test
    void create_returnsNotNull() {
        var trainer = new Trainer(1, TrainingType.RESISTANCE, null);
        var trainee = new Trainee(2, LocalDate.now(), "House");
        var dto = new TrainingDto("trainee-username", "trainer-username", "training-name", LocalDate.now(), 50);

        when(traineeRepository.findFirstByUserUsername(anyString())).thenReturn(Optional.of(trainee));
        when(trainerRepository.findFirstByUserUsername(anyString())).thenReturn(Optional.of(trainer));
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