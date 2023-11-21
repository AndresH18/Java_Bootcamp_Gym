package com.javabootcamp.gym.security;

import com.javabootcamp.gym.security.data.LoginAttempt;
import com.javabootcamp.gym.security.data.LoginAttemptRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class GymAuthenticationProvider extends DaoAuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final LoginAttemptRepository loginAttemptRepository;

    @Autowired
    public GymAuthenticationProvider(UserDetailsService userDetailsService, LoginAttemptRepository loginAttemptRepository, PasswordEncoder passwordEncoder) {
        super(passwordEncoder);
        this.loginAttemptRepository = loginAttemptRepository;
        setUserDetailsService(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        // TODO: use implementation of chat gpt for custom authentication provider.
        //  Check if user is not locked (can login) before calling 'super' method.
        String username = userDetails.getUsername();

        var loginAttempt = new LoginAttempt(username, LocalDateTime.now());

        try {
            checkIsLocked(username);
            super.additionalAuthenticationChecks(userDetails, authentication);
            loginAttempt.setSuccess(true);

        } catch (AuthenticationException e) {
            if (e instanceof BadCredentialsException)
                logger.debug("Bad credentials for '{}'", userDetails.getUsername());

            loginAttempt.setSuccess(false);
            loginAttemptRepository.save(loginAttempt);
            throw e;
        }
    }

    private void checkIsLocked(@NotNull String username) throws LockedException {
        try {
            List<LoginAttempt> recentFailedAttempts =
                    loginAttemptRepository.findByUsernameAndAttemptTimeAfterAndSuccessIsFalse(username,
                            LocalDateTime.now().minusMinutes(5));
            if (recentFailedAttempts.size() >= 3) {
                logger.debug("Locked Account. Account for {} is locked", username);
                throw new LockedException("Account is locked");
            }
        } catch (DataAccessException e) {
            logger.error("Failed to get login attempts", e);
            throw new AuthenticationServiceException("Cannot access login attempts", e);
        }
    }
}
