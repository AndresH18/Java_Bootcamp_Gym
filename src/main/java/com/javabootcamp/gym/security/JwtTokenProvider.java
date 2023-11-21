package com.javabootcamp.gym.security;

import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class JwtTokenProvider extends AbstractJwtTokenProvider {

    public JwtTokenProvider() {
        super(LoggerFactory.getLogger(JwtTokenProvider.class));
    }

    public String getToken(UserDetails userDetails) {
        return getToken(new HashMap<>(), userDetails);
    }

    public String getToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .id(UUID.randomUUID().toString())
                .issuedAt(getDate())
                .expiration(getExpiration())
                .signWith(getKey())
                .compact();
    }

    /**
     * Validates that a String is a JWT token (ignore the redundancy there ^w^ ), is authentic and that it's not expired
     *
     * @param token String jwt to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validate(@NotNull String token) {
        Objects.requireNonNull(token, "Token must be not null");

        try {
            getParser()
                    .parse(token).accept(Jws.CLAIMS);

            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.warn("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("JWT unexpected error", e);
        }

        return false;
    }

    @Deprecated(forRemoval = true)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return (username != null && username.equals(userDetails.getUsername()))
               && !isExpired(token);
    }

    @Nullable
    public String getUsername(@NotNull String token) {
        Objects.requireNonNull(token, "Token cannot be null");

        return getClaim(token, Claims::getSubject);
    }

    @Nullable
    public String getId(@NotNull String token) {
        Objects.requireNonNull(token, "Token cannot be null");

        return getClaim(token, Claims::getId);
    }

    @Nullable
    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public String getTokenSignature(@NotNull String token) {
        return token.split("\\.")[2];
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean isExpired(String token) {
        return getExpiration(token).before(getDate());
    }

    private static Date getExpiration() {
        return Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Returns current system date.
     *
     * @return The system date
     */
    private static Date getDate() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}