package com.javabootcamp.gym.security;

import org.springframework.stereotype.Component;

@Component
public class GymPasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.contentEquals(rawPassword);
    }
}
