package com.javabootcamp.gym.services.helper;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserHelper {
    private UserHelper() {

    }

    public static @NotNull String createUsernamePrefix(@NotNull String firstName, @NotNull String lastName) {
        return firstName.split(" ")[0].toLowerCase() + "."
               + lastName.split(" ")[0].toLowerCase();
    }

    public static User createUser(@NotNull String firstName, @NotNull String lastName, @NotNull String username, long count, PasswordEncoder passwordEncoder) {
        if (count > 0)
            username = username + count;
        var password = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        var passwordHash = passwordEncoder.encode(password);

        return new User(firstName, lastName, username, password);
    }


    public static User createUser(@NotNull String firstName,
                                  @NotNull String lastName,
                                  User.Role role,
                                  @NotNull UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  Logger logger) {

        var username = UserHelper.createUsernamePrefix(firstName, lastName);
        logger.trace("create: username prefix '{}'", username);

        var count = userRepository.countUserByUsernameStartingWith(username);
        logger.trace("create: username count: {}", count);

        var user = UserHelper.createUser(firstName, lastName, username, count, passwordEncoder);
        user.setRole(role);

        user = userRepository.save(user);
        logger.info("create: created user");

        return user;
    }
}