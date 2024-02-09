package com.example.pricetag.config;

import com.example.pricetag.enums.AppUserRole;
import com.example.pricetag.exceptions.UnAuthorizedException;
import com.example.pricetag.services.AuthService;
import com.example.pricetag.utils.ColorLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UnAuthorizedException unAuthorizedException;


    @Bean
    public UserDetailsService userDetailsService() {
        ColorLogger.logInfo("I am inside UserDetailsService");
        return new AuthService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        ColorLogger.logInfo("I am inside SecurityFilterChain");
        return httpSecurity.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unAuthorizedException))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/verify-otp", "/api/v1/auth/forgot-password",
                                "/api/v1/auth/verify-forgot-password-otp", "/api/v1/auth/welcome")
                        .permitAll()
                        .requestMatchers("/api/v1/category/add", "/api/v1/category/{categoryId}", "/api/v1/subcategory/{categoryId}/add",
                                "/api/v1/subcategory/edit", "/api/v1/subcategory/{subCategoryId}", "/api/v1/category/{categoryId}/subcategory/{subCategoryId}/add",
                                "/api/v1//{productId}", "/api/v1/image/upload")
                        .hasRole(AppUserRole.ROLE_ADMIN.name().split("_")[1])
                        .requestMatchers("/user/**")
                        .hasAnyRole(AppUserRole.ROLE_USER.name().split("_")[1],
                                AppUserRole.ROLE_ADMIN.name().split("_")[1])
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UnAuthorizedException unAuthorizedException(ObjectMapper objectMapper) {
        return new UnAuthorizedException(objectMapper);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        ColorLogger.logInfo("I am inside AuthenticationProvider");
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setHideUserNotFoundExceptions(false);
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        ColorLogger.logInfo("I am inside AuthenticationManager");
        return config.getAuthenticationManager();
    }


}