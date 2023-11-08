package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.IDataSource;
import com.javabootcamp.gym.data.model.Trainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * The TrainerDao class provides data access operations for Trainer entities.
 * It allows you to perform CRUD (Create, Retrieve, Update, Delete) operations on Trainer objects
 * in the data source.
 *
 * @see IDao
 */
@Repository
public class TrainerDao implements IDao<Trainer> {

    private final IDataSource dataSource;

    @Autowired
    public TrainerDao(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates a new Trainer in the data source
     *
     * @param trainer The Trainer to create.
     * @return The created Trainer with any auto-generated properties populated.
     */
    @Override
    public @NotNull Trainer create(@NotNull Trainer trainer) {
        return dataSource.create(trainer, Trainer.class);
    }

    /**
     * Retrieves a Trainer from the data source based by its id.
     *
     * @param id The id of the Trainer
     * @return The retrieve Trainer, or null if it was not found in the data source
     * @
     */
    @Override
    public @Nullable Trainer getById(int id) {
        return dataSource.getById(id, Trainer.class);
    }

    /**
     * Updates a Trainer on the data source
     *
     * @param trainer The Trainer to update
     * @return True if the update was successful, False otherwise.
     */
    @Override
    public boolean update(Trainer trainer) {
        return dataSource.update(trainer, Trainer.class);
    }

    /**
     * Deletes a Trainer from the data source.
     *
     * @param trainer The Trainer to delete
     * @return True if the Trainer was deleted successful, false otherwise.
     */
    @Override
    public boolean delete(Trainer trainer) {
        return dataSource.delete(trainer, Trainer.class);
//        throw new UnsupportedOperationException("This operation is not supported");
    }
}