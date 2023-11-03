package com.javabootcamp.gym.data;

import com.javabootcamp.gym.data.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The IDataSource interface provides methods for creating, retrieving, updating, and deleting
 * entities of a specific type.
 */
@Component
public interface IDataSource {
    /**
     * Creates a new entity of the specified type.
     *
     * @param entity The entity to create.
     * @param type   The class representing the entity's type.
     * @param <T>    The type of the entity, which must implement the IModel interface.
     * @return The created entity.
     */
    <T extends IModel> T create(T entity, Class<T> type);

    /**
     * Retrieves an entity of the specified type by its unique identifier.
     *
     * @param id   The unique identifier of the entity.
     * @param type The class representing the entity's type.
     * @param <T>  The type of the entity, which must implement the IModel interface.
     * @return The retrieved entity, or null if not found.
     */

    <T extends IModel> T getById(int id, Class<T> type);

    /**
     * Updates an existing entity of the specified type.
     *
     * @param entity The entity to update.
     * @param type   The class representing the entity's type.
     * @param <T>    The type of the entity, which must implement the IModel interface.
     * @return True if the update was successful, false otherwise.
     */
    <T extends IModel> boolean update(T entity, Class<T> type);

    /**
     * Deletes an existing entity of the specified type.
     *
     * @param entity The entity to delete.
     * @param type   The class representing the entity's type.
     * @param <T>    The type of the entity, which must implement the IModel interface.
     * @return True if the deletion was successful, false otherwise.
     */
    <T extends IModel> boolean delete(T entity, Class<T> type);

    @NotNull
    default <T extends IModel> Stream<T> search(Predicate<T> searchPredicate, Class<T> type) {
        return Stream.empty();
    }
}
