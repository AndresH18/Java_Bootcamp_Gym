package com.javabootcamp.gym.services.user;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.services.delegate.repository.UserRepositoryDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IAuthentication, IUserCreator {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepositoryDelegate userDelegate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepositoryDelegate userDelegate, PasswordEncoder passwordEncoder) {
        this.userDelegate = userDelegate;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(@NotNull String username, @NotNull String password) {
        return userDelegate.existsUsernameAndPasswordHash(username, password);
    }

    @SuppressWarnings({"finally", "ReturnInsideFinallyBlock"})
    @Override
    @Transactional
    public @Nullable User createUser(@NotNull String firstName, @NotNull String lastName, User.Role role) {
        User u = null;
        try {
            var usernamePrefix = createUsernamePrefix(firstName, lastName);
            u = createUser(firstName, lastName, usernamePrefix, role);
        } catch (Exception e) {
            logger.error("Error creating user", e);
        } finally {
            return u;
        }
    }

    public Optional<User> get(@NotNull String username) {
        return userDelegate.findByUsername(username);
    }

    public boolean changePassword(@NotNull String username, @NotNull String oldPassword, @NotNull String newPassword) {

        try {
            var o = userDelegate.findByUsername(username);
            if (o.isEmpty())
                return false;

            var user = o.get();

            if (!passwordEncoder.matches(oldPassword, user.getPassword()))
                return false;

            newPassword = passwordEncoder.encode(newPassword);

            user.setPassword(newPassword);

            userDelegate.save(user);

            return true;
        } catch (Exception e) {
            logger.error("Error updating password", e);
            return false;
        }
    }

    /**
     * Changes the {@link User#isActive} state
     */
    @SuppressWarnings("JavadocReference")
    public Optional<Boolean> setIsActive(@NotNull String username, boolean isActive) {
        try {
            var o = userDelegate.findByUsername(username);
            if (o.isEmpty())
                return Optional.empty();

            var user = o.get();
            user.setActive(isActive);
            userDelegate.save(user);

            return Optional.of(true);

        } catch (Exception e) {
            logger.error("Error setting user active state", e);
            return Optional.of(false);
        }
    }

    private User createUser(String firstName, String lastName, String username,
                            User.Role role) {

        logger.trace("create: username prefix '{}'", username);

        var count = userDelegate.countUsernames(username);
        logger.trace("create: username count: {}", count);

        var user = createUser(firstName, lastName, username, count);
        user.setRole(role);

        user = userDelegate.save(user);
        logger.info("create: created user");

        return user;
    }

    private User createUser(@NotNull String firstName, @NotNull String lastName, @NotNull String username, long count) {
        if (count > 0)
            username = username + count;
        var password = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        var passwordHash = passwordEncoder.encode(password);

        var u = new User(firstName, lastName, username, passwordHash);
        u.setPlainPassword(password);

        return u;
    }

    private static @NotNull String createUsernamePrefix(@NotNull String firstName, @NotNull String lastName) {
        return firstName.split(" ")[0].toLowerCase() + "."
               + lastName.split(" ")[0].toLowerCase();
    }
}