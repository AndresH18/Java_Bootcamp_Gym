package com.javabootcamp.gym.security.services;

import com.javabootcamp.gym.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository repository;

    @Autowired
    public UserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = repository.findByUsernameIgnoreCase(username);

        return u.orElseThrow(() -> new UsernameNotFoundException("User (" + username + ") not found"));
    }
}
