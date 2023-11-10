package com.javabootcamp.gym.services.helper;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

public class UserHelper {
    private UserHelper() {

    }

    public static @NotNull String createUsernamePrefix(@NotNull String firstName, @NotNull String lastName) {
        return firstName.split(" ")[0].toLowerCase() + "."
                + lastName.split(" ")[0].toLowerCase();
    }

    public static User createUser(String firstName, String lastName, String username, long count) {
        username = username + count;
        var password = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        return new User(firstName, lastName, username, password);
    }

    public static User createUser(String firstName, String lastName, UserRepository userRepository, Logger logger) {
        var username = UserHelper.createUsernamePrefix(firstName, lastName);
        logger.trace("create: username prefix '{}'", username);

        var count = userRepository.countUserByUsernameStartingWith(username);
        logger.trace("create: username count: {}", count);

        var user = UserHelper.createUser(firstName, lastName, username, count);
        logger.trace("create: username='{}', password='{}'", user.getUsername(), user.getPassword());

        user = userRepository.save(user);
        logger.info("create: created user");

        return user;
    }
}
