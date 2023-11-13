package com.javabootcamp.gym.services.user;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import com.javabootcamp.gym.services.helper.UserHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IAuthentication, IUserCreator {
    private final UserRepository repository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean authenticate(@NotNull String username, @NotNull String password) {
        return repository.existsUserByUsernameAndPassword(username, password);
    }

    @SuppressWarnings({"finally", "ReturnInsideFinallyBlock"})
    @Override
    public @Nullable User createUser(@NotNull String firstName, @NotNull String lastName) {
        User u = null;
        try {
            u = UserHelper.createUser(firstName, lastName, repository, logger);
        } catch (Exception e) {
            logger.error("Error creating user", e);
        } finally {
            return u;
        }
    }

    @Nullable
    public User get(int id) {
        try {
            return repository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error getting user ({})", id);
            return null;
        }
    }

    public Optional<User> get(@NotNull String username) {
        return repository.findByUsernameIgnoreCase(username);
    }

    public boolean changePassword(@NotNull String username, @NotNull String oldPassword, @NotNull String newPassword) {
        try {
            var o = repository.findByUsernameAndPassword(username, oldPassword);
            if (o.isEmpty())
                return false;

            var user = o.get();

            user.setPassword(newPassword);

            repository.save(user);

            return true;
        } catch (Exception e) {
            logger.error("Error updating user");
            return false;
        }
    }

    /**
     * Changes the {@link User#isActive} state
     */
    @SuppressWarnings("JavadocReference")
    boolean setIsActive(int userId, boolean isActive) {
        throw new UnsupportedOperationException("This method is not implemented yet");
    }
}