package com.javabootcamp.gym.services.user;

import com.javabootcamp.gym.data.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IUserCreator {
    @Nullable
    User createUser(@NotNull String firstName, @NotNull String lastName, User.Role role);
}
