package com.javabootcamp.gym.services.delegate.repository;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@Transactional
public class UserRepositoryDelegate implements IRepositoryDelegate<User> {

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

    private final UserRepository repository;

    @Autowired
    public UserRepositoryDelegate(UserRepository repository) {
        this.repository = repository;
    }
    
    public boolean existsUsernameAndPasswordHash(@NotNull String username, @NotNull String hash) {
        return repository.existsUserByUsernameAndPasswordHash(username, hash);
    }

    public long countUsernames(@NotNull String username) {
        return repository.countUserByUsernameStartingWith(username);
    }

    @Override
    public Optional<User> findByUsername(@NotNull String username) {
        return repository.findByUsernameIgnoreCase(username);
    }

    @Override
    public User save(User user) throws IllegalArgumentException, OptimisticLockingFailureException {
        return repository.save(user);
    }

    @Override
    public void delete(User user) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
