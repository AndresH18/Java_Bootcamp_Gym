package com.javabootcamp.gym.configuration;

import com.javabootcamp.gym.security.GymAuthenticationProvider;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));


        http.authorizeHttpRequests(requests -> {

            requests.requestMatchers(HttpMethod.POST, "/*/register", "/account/login", "/account/logout").permitAll();
            // dev-debug routes
            requests.requestMatchers("/dev/*", "/actuator/**").permitAll();
            // allow swagger to be accessed without authentication
            requests.requestMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/*", "/swagger-resources/*", "/v3/api-docs").permitAll();
//            requests.requestMatchers(HttpMethod.GET,
//                    "/v3/api-docs",
//                    "/v2/api-docs",
//                    "/swagger-resources/**",
//                    "/swagger-ui.html",
//                    "/swagger-ui",
//                    "/configuration/**",
//                    "/webjars/**",
//                    "/swagger-ui/*"
//            ).permitAll();
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
        return new BCryptPasswordEncoder();
//        return new GymPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
