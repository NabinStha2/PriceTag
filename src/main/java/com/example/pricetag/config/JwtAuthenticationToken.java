package com.example.pricetag.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken implements Authentication {

    private final String jwtToken;
    private Authentication authenticated;

    public JwtAuthenticationToken(String jwtToken) {
        this.jwtToken = jwtToken;
        this.authenticated = null;
    }

    public JwtAuthenticationToken(String jwtToken, Authentication authenticated) {
        this.jwtToken = jwtToken;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authenticated != null ? authenticated.getAuthorities() : null;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return authenticated != null ? authenticated.getPrincipal() : null;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated != null && authenticated.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // This method is intentionally left empty
        // Authentication status is set via constructor
    }

    @Override
    public String getName() {
        return authenticated != null ? authenticated.getName() : null;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
