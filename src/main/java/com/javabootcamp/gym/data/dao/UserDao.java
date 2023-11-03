package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.IDataSource;
import com.javabootcamp.gym.data.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.function.Predicate;

/**
 * The UserDao class provides data access operations for User entities.
 * It allows you to perform CRUD (Create, Retrieve, Update, Delete) operations on User objects
 * in the data source.
 *
 * @see com.javabootcamp.gym.data.dao.IDao
 * @see User
 */
@Repository
public class UserDao implements IDao<User> {

    private final IDataSource dataSource;

    @Autowired
    public UserDao(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates a new user in the data source.
     *
     * @param user The user to create.
     * @return The created user with any auto-generated properties populated.
     */
    @Override
    public @NotNull User create(@NotNull User user) {
        return dataSource.create(user, User.class);
    }

    /**
     * Retrieves an entity from the data source based by its id.
     *
     * @param id The id of the user
     * @return The retrieve entity, or null if it was not found in the data source
     */
    @Override
    public @Nullable User getById(int id) {
        return dataSource.getById(id, User.class);
    }

    /**
     * Updates an user on the data source
     *
     * @param user The user to update
     * @return True if the update was successful, False otherwise.
     */
    @Override
    public boolean update(User user) {
        return dataSource.update(user, User.class);
    }

    /**
     * Deletes an user from the data source.
     *
     * @param user The user to delete
     * @return True if the user was deleted successful, false otherwise.
     */
    @Override
    public boolean delete(User user) {
        return dataSource.delete(user, User.class);
    }

    @Override
    public boolean exists(int id) {
        return getById(id) != null;
    }

    @Override
    public long count(Predicate<User> predicate) {
        var stream = dataSource.search(predicate, User.class);

        return stream.count();
    }
}
