package com.example.pricetag.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.pricetag.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthDetails implements UserDetails {
  String userName = null;
  String password = null;
  List<GrantedAuthority> authorities;

  public AuthDetails(User user) {
    userName = user.getEmail();
    password = user.getPassword();

    log.info(user.getAppUserRole().name()
        + " bjba sa s= == = == = = = == == =    / / / / / / // / / /  // / / / / / / / / // / / / / // /");
    authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getAppUserRole().name()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
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