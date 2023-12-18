package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.dto.TrainingDto;
import com.javabootcamp.gym.data.model.Training;
import com.javabootcamp.gym.messaging.TrainingMessage;
import com.javabootcamp.gym.messaging.report.IReportingService;
import com.javabootcamp.gym.services.delegate.repository.TraineeRepositoryDelegate;
import com.javabootcamp.gym.services.delegate.repository.TrainerRepositoryDelegate;
import com.javabootcamp.gym.services.delegate.repository.TrainingRepositoryDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingService {
    private final Logger logger = LoggerFactory.getLogger(TrainingService.class);
    private final TrainingRepositoryDelegate trainingRepositoryDelegate;
    private final TraineeRepositoryDelegate traineeRepositoryDelegate;
    private final TrainerRepositoryDelegate trainerRepositoryDelegate;
    private final @Nullable IReportingService<TrainingMessage> reportingService;

    @Autowired
    public TrainingService(@NotNull TrainingRepositoryDelegate trainingRepositoryDelegate, TraineeRepositoryDelegate traineeRepositoryDelegate,
                           TrainerRepositoryDelegate trainerRepositoryDelegate, @Nullable IReportingService<TrainingMessage> reportingService) {
        this.trainingRepositoryDelegate = trainingRepositoryDelegate;
        this.traineeRepositoryDelegate = traineeRepositoryDelegate;
        this.trainerRepositoryDelegate = trainerRepositoryDelegate;
        this.reportingService = reportingService;
    }

    @Nullable
    @Transactional
    public Training create(@NotNull TrainingDto dto) {
        logger.info("Initiating transaction");
        try {
            var trainee = traineeRepositoryDelegate.findByUsername(dto.traineeUsername());
            var trainer = trainerRepositoryDelegate.findByUsername(dto.trainerUsername());
//            var trainingType = trainingTypeRepository.findFirstByNameIgnoreCase(dto.trainingTypeName());

            if (trainee.isEmpty() || trainer.isEmpty())
                return null;

            var training = new Training(trainer.get(), trainee.get(), trainer.get().getSpecialization(), dto.trainingName(), dto.duration(), dto.date());

            training = trainingRepositoryDelegate.save(training);

            onCreateSuccessful(training);

            return training;
        } catch (Exception e) {
            logger.error("Error creating training", e);
            logger.info("Rolling back transaction");
            // manually rollback transaction in case you don't want to use spring transactional annotation
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            throw e;
        }
    }

    private void onCreateSuccessful(Training training) {
        if (reportingService == null)
            return;

        var user = training.getTrainer().getUser();
        var trainerUsername = user.getUsername();
        var trainerFirstName = user.getFirstName();
        var trainerLastName = user.getLastName();
        var isActive = user.isActive();
        var duration = training.getDuration();
        var year = String.valueOf(training.getDate().getYear());
        var month = TrainingMessage.Month.of(training.getDate().getMonthValue()).getShortName();

        var message = new TrainingMessage(trainerUsername, trainerFirstName, trainerLastName, isActive, duration, year, month, false);

        reportingService.sendMessage(message);
    }
}
