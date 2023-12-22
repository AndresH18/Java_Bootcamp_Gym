package com.javabootcamp.gym.services.user;

import com.javabootcamp.gym.helper.Result;

public interface ICreateService<T, M> {
    Result<T, String> create(M m);
}
