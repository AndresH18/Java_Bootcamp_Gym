package com.javabootcamp.gym.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.function.Function;

public abstract class AbstractJwtTokenProvider {

    @Value("${security.jwt.secret}")
    private String secret;
    private JwtParser parser;

    protected final Logger logger;

    public AbstractJwtTokenProvider(Logger logger) {
        this.logger = logger;
    }

    @NotNull
    protected JwtParser getParser() {
        if (parser == null)
            parser = Jwts.parser()
                    .verifyWith(getKey())
                    .build();
        return parser;
    }

    @NotNull
    protected SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    protected <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = getClaims(token);
        if (claims == null)
            return null;

        return claimsResolver.apply(claims);
    }

    protected Claims getClaims(String token) {
        try {
            Claims claims = getParser()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims;

        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("JWT unexpected error", e);
        }

        return null;
    }
}