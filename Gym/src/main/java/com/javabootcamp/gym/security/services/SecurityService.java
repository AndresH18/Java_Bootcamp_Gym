package com.javabootcamp.gym.security.services;

import com.javabootcamp.gym.data.dto.LoginDto;
import com.javabootcamp.gym.helper.Result;
import com.javabootcamp.gym.security.JwtAuthenticationToken;
import com.javabootcamp.gym.security.JwtTokenProvider;
import com.javabootcamp.gym.security.data.JwtRepository;
import com.javabootcamp.gym.security.data.JwtSecurityToken;
import com.javabootcamp.gym.services.helper.ServiceHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

/**
 * Service class for managing security-related operations.
 *
 * @see JwtTokenProvider
 * @see AuthenticationManager
 */
@Service
public class SecurityService {

    /**
     * Performs a match operation based on the provided authentication, value, and match function.
     * Executes the success or failure supplier based on the result of the match function.
     *
     * @param authentication The authentication object.
     * @param value          The value to be matched against.
     * @param matchFunction  The function defining the match criteria.
     * @param success        The supplier to provide a result if the match is successful.
     * @param failure        The supplier to provide a result if the match is unsuccessful.
     * @param <T>            The type of the result.
     * @return The result of the match operation, either from the success or failure supplier.
     */
    public static <T> T match(@NotNull Authentication authentication,
                              @NotNull String value,
                              @NotNull BiFunction<Authentication, String, Boolean> matchFunction,
                              @NotNull Supplier<T> success,
                              @NotNull Supplier<T> failure) {

        return matchFunction.apply(authentication, value)
//        return match(authentication, value, matchFunction)
                ? success.get()
                : failure.get();

    }

    /**
     * Performs a match operation based on the provided authentication, value, and match function.
     *
     * @param authentication The authentication object.
     * @param value          The value to be matched against.
     * @param matchFunction  The function defining the match criteria.
     * @return {@code true} if the match is successful, {@code false} otherwise.
     */
    public static boolean match(@NotNull Authentication authentication,
                                @NotNull String value,
                                @NotNull BiFunction<Authentication, String, Boolean> matchFunction) {

        return matchFunction.apply(authentication, value);
    }

    /**
     * Performs a match operation and executes a subsequent action if the match is successful.
     *
     * @param authentication The authentication object.
     * @param value          The value to be matched against.
     * @param matchFunction  The function defining the match criteria.
     * @param then           The supplier to provide a result if the match is successful.
     * @param <T>            The type of the result.
     * @return The result of the subsequent action if the match is successful, otherwise {@code null}.
     * @implNote Method content could just return {@code match(authentication, value, matchFunction, then, () -> null)}
     */
    @Nullable
    public static <T> T matchAndThen(@NotNull Authentication authentication,
                                     @NotNull String value,
                                     @NotNull BiFunction<Authentication, String, Boolean> matchFunction,
                                     @NotNull Supplier<T> then) {

        if (matchFunction.apply(authentication, value))
            return then.get();

        return null;
//        return match(authentication, value, matchFunction, then, () -> null);
    }

    /**
     * Performs a match operation based on the provided authentication, value, and match function.
     * Returns the success or failure supplier based on the result of the match function.
     *
     * @param authentication The authentication object.
     * @param value          The value to be matched against.
     * @param matchFunction  The function defining the match criteria.
     * @param success        The supplier to provide a result if the match is successful.
     * @param failure        The supplier to provide a result if the match is unsuccessful.
     * @param <T>            The type of the {@link Supplier<T>}.
     * @return Returns the {@code success} supplier if the match function returns true, otherwise returns the {@code failure} supplier
     */
    public static <T> Supplier<T> matchResponse(@NotNull Authentication authentication,
                                                @NotNull String value,
                                                @NotNull BiFunction<Authentication, String, Boolean> matchFunction,
                                                @NotNull Supplier<T> success,
                                                @NotNull Supplier<T> failure) {

        return matchFunction.apply(authentication, value) ? success : failure;
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRepository jwtRepository;


    @Autowired
    public SecurityService(JwtTokenProvider jwtTokenProvider, AuthenticationManager authManager, JwtRepository jwtRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authManager = authManager;
        this.jwtRepository = jwtRepository;
    }

    /**
     * Creates a JWT if the user can be authenticated
     *
     * @param dto Dto containing login information
     * @return a JWT or null if user can't be authenticated
     */
    @Nullable
    public Result<String, ?> authenticate(LoginDto dto) {
        try {
            Authentication authentication =
                    authManager.authenticate(unauthenticated(dto.username(), dto.password()));

            if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails userDetails) {
                var token = jwtTokenProvider.getToken(userDetails);
                saveToken(token);
                return Result.value(token);
            }

            return Result.error("Wrong username or password");

        } catch (Exception e) {
            logger.error("Cannot authenticate user", e);
            return null;
        }
    }

    /**
     * Retrieves a JwtAuthenticationToken from a Bearer token string.
     *
     * @param token The Bearer token string.
     * @return The JwtAuthenticationToken if the token is valid; otherwise, returns null.
     */
    public JwtAuthenticationToken getBearerToken(@NotNull String token) {
        var username = jwtTokenProvider.getUsername(token);
        if (username == null)
            throw new UsernameNotFoundException("Could not find username inside token");
        return JwtAuthenticationToken.authenticated(token, username, null);
    }

    /**
     * Checks the validity of a JWT token.
     *
     * @param jwtToken The JWT token to validate.
     * @return {@code true} if the token is valid; otherwise, returns {@code false}.
     */
    public boolean isValidToken(@NotNull String jwtToken) {
        var b = jwtTokenProvider.validate(jwtToken);
        if (b) {
            var signature = jwtTokenProvider.getTokenSignature(jwtToken);
            b = jwtRepository.existsBySignatureAndRevokedIsFalse(signature);
        }

        return b;
    }

    public <T> Supplier<T> logout(@NotNull Authentication authentication,
                                  @NotNull Supplier<T> success,
                                  @NotNull Supplier<T> failure) {
        if (authentication instanceof JwtAuthenticationToken auth) {
            var token = auth.getToken();
            var signature = jwtTokenProvider.getTokenSignature(token);

            var jwt = jwtRepository.findBySignature(signature);
            // delete?
            jwt.ifPresent(jwtSecurityToken -> jwtSecurityToken.setRevoked(true));
            jwt.ifPresent(jwtRepository::save);

//            jwt.ifPresent(jwtRepository::delete);

            return success;
        }

        return failure;
    }

    /**
     * Matches the username from the authentication against a provided username.
     * Executes the success or failure supplier based on the result of the match.
     *
     * @param authentication The authentication object.
     * @param username       The username to match against.
     * @param success        The supplier to provide a result if the match is successful.
     * @param failure        The supplier to provide a result if the match is unsuccessful.
     * @param <T>            The type of the result.
     * @return The result of the match operation, either from the success or failure supplier.
     */
    public <T> T matchUsername(@NotNull Authentication authentication,
                               @NotNull String username,
                               @NotNull Supplier<T> success,
                               @NotNull Supplier<T> failure) {

        return match(authentication, username, this::usernameMatch, success, failure);
    }

    /**
     * Matches the username from the authentication against a provided username.
     * Returns the success or failure supplier based on the result of the match.
     *
     * @param authentication The authentication object.
     * @param username       The username to match.
     * @param success        The supplier to provide a result if the match is successful.
     * @param failure        The supplier to provide a result if the match is unsuccessful.
     * @param <T>            The type of the {@link Supplier<T>}.
     * @return Returns the {@code success} supplier if the match function returns true, otherwise returns the {@code failure} supplier
     */
    public <T> Supplier<T> matchUsernameResponse(@NotNull Authentication authentication,
                                                 @NotNull String username,
                                                 @NotNull Supplier<T> success,
                                                 @NotNull Supplier<T> failure) {

        ServiceHelper.requireNonNull(authentication, username, success, failure);
        return matchResponse(authentication, username, this::usernameMatch, success, failure);
    }


    /**
     * Internal method to check if the authentication's username matches the provided username.
     *
     * @param authentication The authentication object.
     * @param username       The username to match against.
     * @return {@code true} if the authentication's username matches the provided username; otherwise, returns {@code false}.
     */
    private boolean usernameMatch(@NotNull Authentication authentication, @NotNull String username) {
        if (authentication instanceof JwtAuthenticationToken authenticationToken) {
            var uName = authenticationToken.getPrincipal().toString();
            if (uName == null)
                uName = jwtTokenProvider.getUsername(authenticationToken.getToken());

            if (uName != null)
                return uName.contentEquals(username);
        }
        return false;
    }

    /**
     * Creates a BiFunction to check if the username in the provided Authentication matches the given username.
     * This function is specifically designed for use in the match method to compare usernames.
     *
     * @return A BiFunction with the signature (Authentication, String) -> Boolean. Returns true if the username
     * in the Authentication matches the given username; otherwise, returns false.
     */
    private BiFunction<Authentication, String, Boolean> usernameMatch() {
        return (authentication, username) -> {
            if (authentication instanceof JwtAuthenticationToken authenticationToken) {
                var uName = authenticationToken.getPrincipal().toString();
                if (uName == null)
                    uName = jwtTokenProvider.getUsername(authenticationToken.getToken());

                if (uName != null)
                    return uName.contentEquals(username);
            }
            return false;
        };
    }

    /**
     * Save the token (or something that identifies the token) in the Database
     *
     * @param token The JWT to save
     */
    private void saveToken(@NotNull String token) {
        var expirationDate = jwtTokenProvider.getExpiration(token);
        Objects.requireNonNull(expirationDate);

        var expiration = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        var signature = jwtTokenProvider.getTokenSignature(token);
        var jwt = new JwtSecurityToken(signature, expiration);

        jwtRepository.save(jwt);
    }

    @Deprecated(forRemoval = true)
    private boolean matchUsername(@NotNull String username, @NotNull JwtAuthenticationToken token) {
        var r = jwtTokenProvider.getUsername(token.getToken());

        return r != null && r.contentEquals(username);
//        return r != null ? r.contentEquals(username) : false;
    }
}