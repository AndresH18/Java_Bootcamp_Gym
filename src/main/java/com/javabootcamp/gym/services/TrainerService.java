package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.dto.TrainerTrainingDto;
import com.javabootcamp.gym.data.dto.TrainingFilterDto;
import com.javabootcamp.gym.data.model.Trainer;
import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.TrainerRepository;
import com.javabootcamp.gym.data.repository.TrainingRepository;
import com.javabootcamp.gym.data.repository.TrainingTypeRepository;
import com.javabootcamp.gym.data.viewmodels.TrainerRegistrationViewModel;
import com.javabootcamp.gym.services.helper.ServiceHelper;
import com.javabootcamp.gym.services.user.UserService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public final class TrainerService {
    private final Logger logger = LoggerFactory.getLogger(TrainerService.class);
    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainerService(@NotNull TrainerRepository trainerRepository, @NotNull UserService userService, TrainingTypeRepository trainingTypeRepository, TrainingRepository trainingRepository) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingRepository = trainingRepository;
    }

    /**
     * Creates a new Trainer
     *
     * @param userId           The {@link User} id
     * @param specializationId The {@link com.javabootcamp.gym.data.model.TrainingType} id
     * @return A new instance of {@link Trainer} with the autogenerated properties populated,
     * or null if it couldn't be created.
     * @see User
     */
    @Nullable
    public Trainer create(int userId, int specializationId) {
        logger.trace("create: userId={}, specializationId={}", userId, specializationId);
        if (userId <= 0 || specializationId <= 0) {
            logger.trace("Invalid id(s)");
            return null;
        }

        var user = userService.get(userId);

        if (user == null) {
            logger.trace("create: user ({}) not found", userId);
            return null;
        }

        var trainingType = trainingTypeRepository.findById(specializationId);
        if (trainingType.isEmpty()) {
            logger.trace("create: training type ({}) not found", specializationId);
            return null;
        }
        logger.info("Creating trainer");

        var trainer = new Trainer(trainingType.get(), user);

        return trainerRepository.save(trainer);
    }

    @Nullable
    public Trainer create(@NotNull String firstName, @NotNull String lastName, String specializationName) {
        logger.trace("create: firstName='{}', lastName='{}', specializationName={}", firstName, lastName, specializationName);

        if (specializationName == null)
            return null;

        var specialization = trainingTypeRepository.findFirstByNameIgnoreCase(specializationName);
        if (specialization.isEmpty())
            return null;

        var user = userService.createUser(firstName, lastName);

        return trainerRepository.save(new Trainer(specialization.get(), user));
    }

    @Nullable
    public Trainer create(@NotNull TrainerRegistrationViewModel vm) {
        if (vm.getSpecialization() != null && !vm.getSpecialization().isBlank()) {
            return create(vm.getFirstName(), vm.getLastName(), vm.getSpecialization());
        } else {
            return create(vm.getFirstName(), vm.getLastName(), vm.getSpecializationId());
        }

    }

    /**
     * Create a new User and a Trainer with the User id
     *
     * @param firstName        The User firstname
     * @param lastName         The User lastname
     * @param specializationId the Trainer Specialization id
     * @return A new instance of {@link Trainer} with autogenerated properties populated, including the userId
     * or null if it couldn't be created.
     * @see User
     * @see com.javabootcamp.gym.data.model.TrainingType
     */
    @Nullable
    public Trainer create(@NotNull String firstName, @NotNull String lastName, int specializationId) {
        logger.trace("create: firstName='{}', lastName='{}', specializationId={}", firstName, lastName, specializationId);

        if (specializationId <= 0)
            return null;

        var specialization = trainingTypeRepository.findById(specializationId);
        if (specialization.isEmpty())
            return null;

        var user = userService.createUser(firstName, lastName);

        return trainerRepository.save(new Trainer(specialization.get(), user));
    }

    @Nullable
    public Trainer getById(int id) {
        logger.trace("getById: id={}", id);

        return ServiceHelper.findById(id, trainerRepository);
    }

    @Nullable
    public Trainer getByUsername(@NotNull String username) {
        logger.trace("getByUsername: username={}", username);

        var user = userService.get(username);

        return user.map(User::getTrainer).orElse(null);
    }

    @NotNull
    public Optional<List<TrainerTrainingDto>> getTrainings(@NotNull String username, @NotNull TrainingFilterDto dto) {
        try {
            var r = trainingRepository.getTrainerTrainings(username, dto.periodFrom(), dto.periodTo(), dto.trainingName(), dto.name());

            var l = r.stream()
                    .map(t -> new TrainerTrainingDto(
                            t.getName(),
                            t.getDate(),
                            t.getTrainingType().getName(),
                            t.getDuration(),
                            t.getTrainee().getUser().getUsername()));

            return Optional.of(l.toList());
        } catch (Exception e) {
            logger.error("Error getting trainer trainings", e);
            return Optional.empty();
        }
    }

    boolean update(@NotNull Trainer trainer) {
        return ServiceHelper.update(trainer, trainerRepository);
    }
}
