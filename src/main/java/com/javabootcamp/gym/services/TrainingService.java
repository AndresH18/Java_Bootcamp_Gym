package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.dto.TrainingDto;
import com.javabootcamp.gym.data.model.Training;
import com.javabootcamp.gym.data.repository.TraineeRepository;
import com.javabootcamp.gym.data.repository.TrainerRepository;
import com.javabootcamp.gym.data.repository.TrainingRepository;
import com.javabootcamp.gym.services.helper.ServiceHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
    private final Logger logger = LoggerFactory.getLogger(TrainingService.class);
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainingService(@NotNull TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Nullable
    public Training create(@NotNull TrainingDto dto) {
        try {
            var trainee = traineeRepository.findFirstByUserUsername(dto.traineeUsername());
            var trainer = trainerRepository.findFirstByUserUsername(dto.trainerUsername());
//            var trainingType = trainingTypeRepository.findFirstByNameIgnoreCase(dto.trainingTypeName());

            if (trainee.isEmpty() || trainer.isEmpty())
                return null;

            var training = new Training(trainer.get(), trainee.get(), trainer.get().getSpecialization(), dto.trainingName(), dto.duration(), dto.date());

            training = trainingRepository.save(training);

            return training;
        } catch (Exception e) {
            logger.error("Error creating training", e);
            return null;
        }
    }
}
