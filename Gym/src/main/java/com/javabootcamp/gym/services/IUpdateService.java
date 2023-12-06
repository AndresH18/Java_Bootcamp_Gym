package com.javabootcamp.gym.services;

/**
 * @param <T> The type for the update dto
 */
public interface IUpdateService<T> {
    boolean update(String username, T dto);
}
