package com.javabootcamp.gym.services.helper;

import com.javabootcamp.gym.data.model.ICopy;
import com.javabootcamp.gym.data.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

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
