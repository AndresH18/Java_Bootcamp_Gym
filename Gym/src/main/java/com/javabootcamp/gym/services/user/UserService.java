package com.javabootcamp.gym.services.user;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import com.javabootcamp.gym.services.helper.UserHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IAuthentication, IUserCreator {
    private final UserRepository repository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(@NotNull String username, @NotNull String password) {
        return repository.existsUserByUsernameAndPassword(username, password);
    }

    @SuppressWarnings({"finally", "ReturnInsideFinallyBlock"})
    @Override
    public @Nullable User createUser(@NotNull String firstName, @NotNull String lastName, User.Role role) {
        User u = null;
        try {
            u = UserHelper.createUser(firstName, lastName, role, repository, passwordEncoder, logger);
        } catch (Exception e) {
            logger.error("Error creating user", e);
        } finally {
            return u;
        }
    }

    public Optional<User> get(@NotNull String username) {
        return repository.findByUsernameIgnoreCase(username);
    }

    public boolean changePassword(@NotNull String username, @NotNull String oldPassword, @NotNull String newPassword) {

        try {
            oldPassword = passwordEncoder.encode(oldPassword);
            newPassword = passwordEncoder.encode(newPassword);

            var o = repository.findByUsernameAndPassword(username, oldPassword);
            if (o.isEmpty())
                return false;

            var user = o.get();

            user.setPassword(newPassword);

            repository.save(user);

            return true;
        } catch (Exception e) {
            logger.error("Error updating user", e);
            return false;
        }
    }

    /**
     * Changes the {@link User#isActive} state
     */
    @SuppressWarnings("JavadocReference")
    public Optional<Boolean> setIsActive(@NotNull String username, boolean isActive) {
        try {
            var o = repository.findByUsernameIgnoreCase(username);
            if (o.isEmpty())
                return Optional.empty();

            var user = o.get();
            user.setActive(isActive);
            repository.save(user);

            return Optional.of(true);

        } catch (Exception e) {
            logger.error("Error setting user active state", e);
            return Optional.of(false);
        }
    }
}