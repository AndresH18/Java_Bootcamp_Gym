package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.IDataSource;
import com.javabootcamp.gym.data.model.Trainee;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * The TraineeDao class provides data access operations for Trainee entities.
 * It allows you to perform CRUD (Create, Retrieve, Update, Delete) operations on Trainee objects
 * in the data source.
 *
 * @see IDao
 * @see Trainee
 */
@Repository
public class TraineeDao implements IDao<Trainee> {
    private final IDataSource dataSource;

    @Autowired
    public TraineeDao(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates a new Trainee in the data source
     *
     * @param trainee The Trainee to create.
     * @return The created Trainee with any auto-generated properties populated.
     */
    @Override
    public @NotNull Trainee create(@NotNull Trainee trainee) {
        return dataSource.create(trainee, Trainee.class);
    }

    /**
     * Retrieves a Trainee from the data source based by its id.
     *
     * @param id The id of the entity
     * @return The retrieve Trainee, or null if it was not found in the data source
     */
    @Override
    public @Nullable Trainee getById(int id) {
        return dataSource.getById(id, Trainee.class);
    }

    /**
     * Updates a Trainee on the data source
     *
     * @param trainee The Trainee to update
     * @return True if the update was successful, False otherwise.
     */
    @Override
    public boolean update(Trainee trainee) {
        return dataSource.update(trainee, Trainee.class);
    }

    /**
     * Deletes a Trainee from the data source.
     *
     * @param trainee The Trainee to delete
     * @return True if the Trainee was deleted successful, false otherwise.
     */
    @Override
    public boolean delete(Trainee trainee) {
        return dataSource.delete(trainee, Trainee.class);
    }
}
