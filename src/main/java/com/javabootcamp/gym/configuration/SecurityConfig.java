package com.javabootcamp.gym.configuration;

import com.javabootcamp.gym.security.GymAuthenticationProvider;
import com.javabootcamp.gym.security.GymPasswordEncoder;
import com.javabootcamp.gym.security.JwtAuthenticationFilter;
import com.javabootcamp.gym.security.data.LoginAttemptRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        // TODO: delete this
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(requests -> {

            requests.requestMatchers(HttpMethod.POST, "/*/register", "/account/login", "/account/logout").permitAll();

            requests.anyRequest().authenticated();
        });

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//        http.logout(logout -> {
//            logout.permitAll();
//            logout.logoutUrl("/account/logout");
//        });

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            LoginAttemptRepository loginAttemptRepository
    ) {
        var authenticationProvider = new GymAuthenticationProvider(userDetailsService, loginAttemptRepository, passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO: use safe password encoder
        return new GymPasswordEncoder();
    }
}
