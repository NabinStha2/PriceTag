package com.example.pricetag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.example.pricetag.config.exceptions.ApplicationException;
import com.example.pricetag.dto.AuthDto;
import com.example.pricetag.dto.AuthResponseDto;
import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.entity.User;
import com.example.pricetag.services.AuthService;
import com.example.pricetag.services.JwtService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthService authService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtService jwtService;

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome to Spring Security tutorials !!";
  }

  @PostMapping("/register")
  public ResponseEntity<CommonResponseDto> register(@RequestBody User user) throws ApplicationException {
    return new ResponseEntity<CommonResponseDto>(authService.register(user), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public AuthResponseDto login(@RequestBody AuthDto authDto) throws ApplicationException {
    Authentication authenticate = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));
    if (authenticate.isAuthenticated()) {
      AuthResponseDto authResponseDto = AuthResponseDto.builder().token(jwtService.generateToken(authDto.getEmail()))
          .build();
      return authResponseDto;
    } else {
      throw new UsernameNotFoundException("Invalid user request");
    }
  }

  @GetMapping("/getUsers")
  @PreAuthorize("hasAuthority('USER')")
  public List<User> getAllUsers() throws ApplicationException {
    return authService.getAllUser();
  }

  @GetMapping("/getUsers/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public User getAllUsers(@PathVariable Integer id) throws ApplicationException {
    return authService.getUser(id);
  }
}