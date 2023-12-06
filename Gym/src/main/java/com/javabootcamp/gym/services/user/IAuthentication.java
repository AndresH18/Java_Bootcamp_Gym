package com.javabootcamp.gym.services.user;

public interface IAuthentication {

    boolean authenticate(String username, String password);
}
