package com.javabootcamp.gym.services.helper;

import com.javabootcamp.gym.data.model.IModel;
import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class UpdateServiceHelper {
    private UpdateServiceHelper() {
    }

    private void updateUser(@NotNull String username, @NotNull String firstName, @NotNull String lastName, boolean isActive, @NotNull UserRepository userRepository) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(userRepository);
    }

    /**
     * Creates a {@code Consumer<User>} to update the properties of a user.
     *
     * @param firstName The new first name for the user. Must not be null.
     * @param lastName  The new last name for the user. Must not be null.
     * @param isActive  The new activation status for the user.
     * @return A {@code Consumer<User>} that updates the user with the provided properties.
     * @throws NullPointerException if firstName or lastName is null.
     */
    public static Consumer<User> updateUser(@NotNull String firstName, @NotNull String lastName, boolean isActive) {
        Objects.requireNonNull(firstName, "First name must not be null");
        Objects.requireNonNull(lastName, "Last name must not be null");

        return user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setActive(isActive);
        };
    }
}
