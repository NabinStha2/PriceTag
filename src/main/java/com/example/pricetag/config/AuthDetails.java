package com.example.pricetag.config;

import com.example.pricetag.entity.User;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AuthDetails implements UserDetails {
    String userName = null;
    String password = null;
    Long id = null;
    List<GrantedAuthority> authorities;

    public AuthDetails(User user) {
        userName = user.getEmail();
        password = user.getPassword();
        id = user.getId();

        ColorLogger.logInfo("AuthDetails :: AppUserRole :: " +
                user.getAppUserRole().name());
        authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getAppUserRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ColorLogger.logInfo("AuthDetails :: authorities :: " + authorities.toString());
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}