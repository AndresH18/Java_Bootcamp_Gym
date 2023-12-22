package com.javabootcamp.gym.services.delegate.repository;

import com.javabootcamp.gym.data.model.Training;
import com.javabootcamp.gym.data.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
public class TrainingRepositoryDelegate {
    private final TrainingRepository repository;

    @Autowired
    public TrainingRepositoryDelegate(TrainingRepository repository) {
        this.repository = repository;
    }

    public List<Training> getTraineeTrainings(String username, LocalDate dateFrom, LocalDate dateTo, String trainingTypeName, String trainerUsername) {
        return repository.getTraineeTrainings(username, dateFrom, dateTo, trainingTypeName, trainerUsername);
    }

    public List<Training> getTrainerTrainings(String username, LocalDate dateFrom, LocalDate dateTo, String trainingTypeName, String trainerUsername) {
        return repository.getTrainerTrainings(username, dateFrom, dateTo, trainingTypeName, trainerUsername);
    }

    public Training save(Training training) {
        return repository.save(training);
    }
}
