package com.example.pricetag.config;

import com.example.pricetag.enums.AppUserRole;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Public endpoints that don't require authentication
    private static final String[] PUBLIC_ENDPOINTS = {"/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/verify-otp", "/api/v1/auth/forgot-password", "/api/v1/auth/verify-forgot-password-otp", "/api/v1/auth/welcome", "/", "/actuator/health", "/swagger-ui/**", "/v3/api-docs/**"};

    // Admin-only endpoints
    private static final String[] ADMIN_ENDPOINTS = {"/api/v1/category/add", "/api/v1/subcategory/{categoryId}/add", "/api/v1/subcategory/edit", "/api/v1/product/category/{categoryId}/subcategory/{subCategoryId}/add", "/api/v1/image/upload"};

    // User-accessible endpoints (both USER and ADMIN roles)
    private static final String[] USER_ENDPOINTS = {"/user/**", "/api/v1/category/{categoryId}", "/api/v1/subcategory/{subCategoryId}", "/api/v1/product/{productId}"};

    // Helper method to extract role name
    private String extractRole(AppUserRole role) {
        return role.name().substring(role.name().indexOf("_") + 1);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, JwtFilter jwtFilter) throws Exception {
        ColorLogger.logInfo("Configuring security filter chain");
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_ENDPOINTS)
                        .permitAll()
                        .requestMatchers(ADMIN_ENDPOINTS)
                        .hasRole(extractRole(AppUserRole.ROLE_ADMIN))
                        .requestMatchers(USER_ENDPOINTS)
                        .hasAnyRole(extractRole(AppUserRole.ROLE_USER), extractRole(AppUserRole.ROLE_ADMIN))
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider(passwordEncoder, userDetailsService))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Stronger encryption
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        ColorLogger.logInfo("I am inside AuthenticationManager");
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        ColorLogger.logInfo("I am inside AuthenticationProvider");
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Configure allowed origins (more secure than wildcard)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000",  // React dev server
                "http://localhost:8080",  // Spring Boot server
                "https://yourdomain.com"  // Production domain
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}