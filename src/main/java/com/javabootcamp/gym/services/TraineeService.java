package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.dao.TraineeDao;
import com.javabootcamp.gym.data.dao.UserDao;
import com.javabootcamp.gym.data.model.Trainee;
import com.javabootcamp.gym.data.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class TraineeService {
    private final TraineeDao traineeDao;
    private final UserDao userDao;


    @Autowired
    public TraineeService(TraineeDao traineeDao, UserDao userDao) {
        this.traineeDao = traineeDao;
        this.userDao = userDao;
    }

    /**
     * Creates a new Trainee
     *
     * @param userId      of {@link User}
     * @param dateOfBirth The date of birth
     * @param address     The Trainee address
     * @return A new instance of {@link Trainee} with autogenerated properties populated, or null
     * if the {@code dateOfBirth} is incorrect or there was a problem when writing to the datasource
     * @see User
     */
    @Nullable
    public Trainee create(int userId, @NotNull Date dateOfBirth, @NotNull String address) {
        if (isNotValidDate(dateOfBirth)) return null;

        var trainee = new Trainee(userId, dateOfBirth, address);

        return traineeDao.create(trainee);
    }

    /**
     * Create a new User and a Trainee with the User id
     *
     * @param firstName   The User firstname
     * @param lastName    The User lastname
     * @param dateOfBirth The Trainee date of birth
     * @param address     The Trainee address
     * @return A new instance of {@link Trainee} with autogenerated properties populated, including the userId, or null
     * * if the {@code dateOfBirth} is incorrect or there was a problem when writing to the datasource
     * @see User
     */
    @Nullable
    public Trainee create(@NotNull String firstName, @NotNull String lastName, @NotNull Date dateOfBirth, @NotNull String address) {
        if (isNotValidDate(dateOfBirth)) return null;

        var user = new User(firstName, lastName);
        var username = firstName.split(" ")[0].toLowerCase()
                + lastName.split(" ")[0].toLowerCase();

        var count = userDao.count(u -> u.getUsername().matches(username + "\\d*"));

        user.setUsername(username + count);
        user.setPassword(UUID.randomUUID().toString().replace("-", "").substring(0, 10));

        user = userDao.create(user);

        return traineeDao.create(new Trainee(user.getId(), dateOfBirth, address));
    }

    @Nullable
    public Trainee getById(int id) {
        if (id <= 0) return null;

        return traineeDao.getById(id);
    }

    public boolean update(@NotNull Trainee trainee) {
        if (trainee.getId() <= 0) return false;

        if (!traineeDao.exists(trainee.getId()))
            return false;

        return traineeDao.update(trainee);
    }

    public boolean delete(int id) {
        var t = getById(id);
        if (t == null) return false;

        return traineeDao.delete(t);
    }

    /**
     * Checks if a date is greater than the current date.
     *
     * @param date The date to compare
     * @return true if the date before the current date.
     */
    private boolean isNotValidDate(Date date) {
        return !new Date().after(date);
    }
}
