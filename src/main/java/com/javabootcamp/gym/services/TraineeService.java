package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.model.Trainee;
import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.TraineeRepository;
import com.javabootcamp.gym.data.viewmodels.TraineeRegistrationViewModel;
import com.javabootcamp.gym.services.helper.ServiceHelper;
import com.javabootcamp.gym.services.user.UserService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public final class TraineeService {
    private final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    private final TraineeRepository traineeRepository;
    private final UserService userService;

    @Autowired
    public TraineeService(@NotNull TraineeRepository traineeRepository, @NotNull UserService userService) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
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
    public Trainee create(int userId, @NotNull LocalDate dateOfBirth, @NotNull String address) {
        logger.trace("create: userId={}, dateOfBirth={}, address='{}'", userId, dateOfBirth, address);
        if (!ServiceHelper.isValidDate(dateOfBirth)) {
            logger.info("Invalid date");
            return null;
        }

        var user = userService.get(userId);

        if (user == null) {
            logger.trace("create: user ({}) not found", userId);
            return null;
        }
        logger.info("Creating trainer");

        var trainee = new Trainee(dateOfBirth, address, user);

        return traineeRepository.save(trainee);
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
    public Trainee create(@NotNull String firstName, @NotNull String lastName, @NotNull LocalDate dateOfBirth, @NotNull String address) {
        logger.trace("create: firstName='{}', lastName={}, dateOfBirth={}, address='{}'", firstName, lastName, dateOfBirth, address);

        if (!ServiceHelper.isValidDate(dateOfBirth)) return null;

        var user = userService.createUser(firstName, lastName);

        return traineeRepository.save(new Trainee(dateOfBirth, address, user));
    }

    @Nullable
    public Trainee create(TraineeRegistrationViewModel vm) {
        return create(vm.getFirstName(), vm.getLastName(), vm.getDateOfBirth(), vm.getAddress());
    }

    @Nullable
    public Trainee getById(int id) {
        logger.trace("getById: id={}", id);

        return ServiceHelper.findById(id, traineeRepository);
    }

    public boolean update(@NotNull Trainee trainee) {
        return ServiceHelper.update(trainee, traineeRepository);
    }

    public boolean delete(int id) {
        logger.trace("delete: id={}", id);
        if (id <= 0)
            return false;

        traineeRepository.deleteById(id);
        return true;
    }


}
