package com.javabootcamp.gym.security;

import com.javabootcamp.gym.security.services.SecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_TYPE_PREFIX = "Bearer ";

    private final SecurityService security;

    @Autowired
    public JwtAuthenticationFilter(SecurityService security) {
        this.security = security;
    }


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        String token = getJwt(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }


        if (security.isValidToken(token)) {
            var bearer = security.getBearerToken(token);
            bearer.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            var context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(bearer);

            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }

    @Nullable
    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_TYPE_PREFIX))
            return authHeader.substring(TOKEN_TYPE_PREFIX.length());

        return null;
    }
}
