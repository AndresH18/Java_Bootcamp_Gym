package com.javabootcamp.gym.security.services;

import com.javabootcamp.gym.services.delegate.repository.UserRepositoryDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepositoryDelegate delegate;

    @Autowired
    public UserDetailsService(UserRepositoryDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = delegate.findByUsername(username);

        return u.orElseThrow(() -> new UsernameNotFoundException("User (" + username + ") not found"));
    }
}
