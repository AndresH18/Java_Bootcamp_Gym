package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.model.Training;
import com.javabootcamp.gym.data.repository.TraineeRepository;
import com.javabootcamp.gym.data.repository.TrainerRepository;
import com.javabootcamp.gym.data.repository.TrainingRepository;
import com.javabootcamp.gym.data.repository.TrainingTypeRepository;
import com.javabootcamp.gym.services.helper.ServiceHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TrainingService {
    private final Logger logger = LoggerFactory.getLogger(TrainingService.class);
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingService(@NotNull TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Nullable
    public Training create(int traineeId, int trainerId, int trainingTypeId, String name, int duration, @NotNull LocalDate date) {
        logger.trace("create: traineeId={}, trainerId={}, trainingTypeId={}, duration={}, date={}", traineeId, trainerId, trainingTypeId, duration, date);
        if (trainerId <= 0 || traineeId <= 0 || trainingTypeId <= 0 || duration <= 0 || !ServiceHelper.isValidDate(date)) {
            logger.trace("create: invalid id(s)");
            return null;
        }
        var trainee = ServiceHelper.findById(traineeId, traineeRepository);
        var trainer = ServiceHelper.findById(trainerId, trainerRepository);
        var trainingType = ServiceHelper.findById(trainingTypeId, trainingTypeRepository);

        if (ServiceHelper.areAnyNull(trainee, trainer, trainingType))
            return null;

        logger.info("Creating training");
        var training = new Training(trainer, trainee, trainingType, name, duration, date);

        return trainingRepository.save(training);
    }

    @Nullable
    public Training getById(int id) {
        logger.trace("getById: id={}", id);

        return ServiceHelper.findById(id, trainingRepository);
    }
}
