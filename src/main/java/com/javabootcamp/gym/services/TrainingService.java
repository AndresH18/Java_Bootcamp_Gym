package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.dao.TrainingDao;
import com.javabootcamp.gym.data.model.Training;
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
    private final TrainingDao dao;

    @Autowired
    public TrainingService(@NotNull TrainingDao dao) {
        this.dao = dao;
    }

    @Nullable
    public Training create(int traineeId, int trainerId, int trainingTypeId, String name, int duration, @NotNull LocalDate date) {
        logger.trace("create: traineeId={}, trainerId={}, trainingTypeId={}, duration={}, date={}", traineeId, trainerId, trainingTypeId, duration, date);
        if (trainerId <= 0 || traineeId <= 0 || trainingTypeId <= 0 || duration <= 0 || !isValidDate(date)) {
            logger.trace("create: invalid id(s)");
            return null;
        }

        logger.info("Creating training");
        var training = new Training(traineeId, trainerId, trainingTypeId, name, date, duration);

        return dao.create(training);
    }

    @Nullable
    public Training getById(int id) {
        logger.trace("getById: id={}", id);
        logger.info("Retrieving training");
        if (id <= 0) {
            logger.trace("getById: invalid id");
            return null;
        }

        return dao.getById(id);
    }

    /**
     * Checks if a date is greater than the current date.
     *
     * @param date The date to compare
     * @return true if the date before the current date.
     */
    private boolean isValidDate(LocalDate date) {
        return LocalDate.now().isAfter(date);
    }
}
