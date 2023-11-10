package com.javabootcamp.gym.services.helper;

import com.javabootcamp.gym.data.model.User;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
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
}
