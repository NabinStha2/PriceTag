package com.example.pricetag.services;

import com.example.pricetag.auth.AuthDetails;
import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.entity.User;
import com.example.pricetag.enums.AppUserRole;
import com.example.pricetag.repository.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
  @Autowired
  private AuthRepository authRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = authRepository.findByEmail(username);
    return user.map(AuthDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
  }

  public CommonResponseDto register(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setAppUserRole(AppUserRole.USER);
    authRepository.save(user);
    return CommonResponseDto.builder().message("User has been created successfully").build();
  }

  public List<User> getAllUser() {
    return authRepository.findAll();
  }

  public User getUser(Integer id) {
    return authRepository.findById(id).get();
  }
}