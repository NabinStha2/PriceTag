package com.example.pricetag.config;

import com.example.pricetag.entity.Role;
import com.example.pricetag.entity.User;
import com.example.pricetag.utils.ColorLogger;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthDetails implements UserDetails {
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isActive;
    private final String userName;
    private final String password;
    @Getter
    private final Long id;

    public AuthDetails(User user) {
        this.userName = user.getEmail();
        this.password = user.getPassword();
        this.id = user.getId();
        this.isActive = user.getIsActive();

        // ColorLogger.logInfo("AuthDetails :: AppUserRole :: " +
        // user.getAppUserRole().name());
//        authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getAppUserRole().name()));

        this.authorities = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        ColorLogger.logInfo("AuthDetails :: User :: " + userName + " Roles :: " + authorities);

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
        return isActive;
    }

}