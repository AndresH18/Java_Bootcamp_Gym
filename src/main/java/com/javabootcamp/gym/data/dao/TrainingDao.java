package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.IDataSource;
import com.javabootcamp.gym.data.model.Training;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * The TrainingDao class provides data access operations for Training entities.
 * It allows you to perform CRUD (Create, Retrieve, Update, Delete) operations on Training objects
 * in the data source.
 *
 * @see IDao
 */
@Repository
public class TrainingDao implements IDao<Training> {
    private final IDataSource dataSource;

    @Autowired
    public TrainingDao(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates a new Training in the data source
     *
     * @param training The Training to create.
     * @return The created Training with any auto-generated properties populated.
     */
    @Override
    public @NotNull Training create(@NotNull Training training) {
        return dataSource.create(training, Training.class);
    }

    /**
     * Retrieves a Training from the data source based by its id.
     *
     * @param id The id of the Training
     * @return The retrieve Training, or null if it was not found in the data source
     */
    @Override
    public @Nullable Training getById(int id) {
        return dataSource.getById(id, Training.class);
    }

    /**
     * Updates a Training on the data source
     *
     * @param training The Training to update
     * @return True if the update was successful, False otherwise.
     */
    @Override
    public boolean update(Training training) {
        return dataSource.update(training, Training.class);
    }

    /**
     * Deletes a Training from the data source.
     *
     * @param training The Training to delete
     * @return True if the Training was deleted successful, false otherwise.
     */
    @Override
    public boolean delete(Training training) {
        return dataSource.delete(training, Training.class);
    }
}
