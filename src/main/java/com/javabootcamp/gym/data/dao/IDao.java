package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.function.Predicate;

/**
 * This interface defines the basic operations for a Data Access Object (DAO) to interact
 * with a data source for a specific entity type, represented by the type parameter 'T'.
 *
 * @param <T> The type of entity this DAO is responsible for.
 */
@Repository
public interface IDao<T extends IModel> {
    /**
     * Creates a new entity in the data source.
     *
     * @param entity The entity to create.
     * @return The created entity with any auto-generated properties populated.
     */
    @NotNull
    T create(@NotNull T entity);

    /**
     * Retrieves an entity from the data source based by its id.
     *
     * @return The retrieve entity, or null if it was not found in the data source
     */
    @Nullable T getById(int id);

    /**
     * Updates an entity on the data source
     *
     * @param entity The entity to update
     * @return True if the update was successful, False otherwise.
     */
    boolean update(T entity);

    /**
     * Deletes an entity from the data source.
     *
     * @param entity The entity to delete
     * @return True if the entity was deleted successful, false otherwise.
     */
    boolean delete(T entity);

    /**
     * Checks if the entity with the id exists in the datasource
     *
     * @param id The entity id
     * @return True if the entity exists in the data source
     */
    default boolean exists(int id) {
        return getById(id) != null;
    }

    default long count(Predicate<T> predicate) {
        return 0;
    }
}
