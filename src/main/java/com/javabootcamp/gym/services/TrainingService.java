package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.dao.TrainingDao;
import com.javabootcamp.gym.data.model.Training;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TrainingService {
    private final TrainingDao dao;

    @Autowired

    public TrainingService(TrainingDao dao) {
        this.dao = dao;
    }

    @Nullable
    public Training create(int traineeId, int trainerId, int trainingType, String name, int duration, @NotNull Date date) {

        if (trainerId <= 0 || traineeId <= 0 || trainingType <= 0 || duration <= 0 || !isValidDate(date))
            return null;

        var training = new Training(traineeId, trainerId, trainingType, name, date, duration);

        return dao.create(training);
    }

    /**
     * Checks if a date is greater than the current date.
     *
     * @param date The date to compare
     * @return true if the date before the current date.
     */
    private boolean isValidDate(Date date) {
        return new Date().after(date);
    }
}
