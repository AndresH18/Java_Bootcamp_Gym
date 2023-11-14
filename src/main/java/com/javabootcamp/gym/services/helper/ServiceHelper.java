package com.javabootcamp.gym.services.helper;

import com.javabootcamp.gym.data.model.ICopy;
import com.javabootcamp.gym.data.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ServiceHelper {
    private ServiceHelper() {
    }

    public static <T extends IModel & ICopy<T>> boolean update(T t, JpaRepository<T, Integer> repo) {
        if (t.getId() <= 0)
            return false;

        var optional = repo.findById(t.getId());
        if (optional.isEmpty())
            return false;

        t = optional.get().copyFrom(t);
        t = repo.save(t);

        return true;
    }

    public static <T extends IModel> @Nullable T findById(int id, JpaRepository<T, Integer> repo) {
        if (id <= 0)
            return null;

        var t = repo.findById(id);

        return t.orElse(null);
    }

    /**
     * Applies a function to retrieve an entity based on the provided username,
     * performs actions on the entity using a consumer, and returns the entity.
     *
     * @param <T>      The type of the entity extending {@code IModel}.
     * @param <R>      The type of the entity to which the function will be applied
     * @param username The username to retrieve the entity.
     * @param function The function to retrieve the entity based on the username.
     * @param consumer The consumer to perform actions on the retrieved entity.
     * @param convert  The function that will convert from {@code <T>} to {@code <R>}
     * @return The retrieved entity after applying the actions, or {@code null}
     * if the entity is not found based on the provided username.
     * @throws NullPointerException if any of the parameters (username, function, consumer) is null.
     */
    public static <T extends IModel, R> T apply(@NotNull String username,
                                                @NotNull Function<String, Optional<T>> function,
                                                @NotNull Consumer<R> consumer,
                                                @NotNull Function<T, R> convert) {
        Objects.requireNonNull(username, "Username must not be null");
        Objects.requireNonNull(function, "Function must not be null");
        Objects.requireNonNull(consumer, "Consumer must not be null");

        // Apply the function to retrieve the entity based on the username
        var optionalT = function.apply(username);

        // If the entity is not found, return null
        if (optionalT.isEmpty())
            return null;

        // Get the retrieved entity
        var m = optionalT.get();

        // Apply convert function
        var c = convert.apply(m);

        // Apply the consumer to perform actions on the entity
        consumer.accept(c);

        // Return the entity after applying the actions
        return m;
    }

    public static boolean areAnyNull(Object... objects) {
        for (Object object : objects) {
            if (object == null)
                return true;
        }
        return false;
    }

    /**
     * Checks if a date is valid.
     *
     * @param date The date to compare
     * @return true if the date is valid.
     */
    public static boolean isValidDate(LocalDate date) {
        return LocalDate.now().isAfter(date);
    }

}
