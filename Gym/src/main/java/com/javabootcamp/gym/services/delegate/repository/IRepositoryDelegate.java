package com.javabootcamp.gym.services.delegate.repository;

import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

public interface IRepositoryDelegate<T> {
    T save(T t) throws IllegalArgumentException, OptimisticLockingFailureException;


    //    T get(int id);
    Optional<T> findByUsername(String username);

    void delete(T t) throws IllegalArgumentException, OptimisticLockingFailureException;

//    default <E> T saveWith(Function<E, IRepositoryDelegate<E>> delegateFunction) {
//        throw new UnsupportedOperationException();
//    }
}
