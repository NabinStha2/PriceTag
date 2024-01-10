package com.example.pricetag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricetag.dto.AuthDto;
import com.example.pricetag.dto.ForgotPasswordDto;
import com.example.pricetag.dto.RefreshTokenDto;
import com.example.pricetag.entity.User;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.AuthResponseDto;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.AuthService;
import com.example.pricetag.utils.ColorLogger;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthService authService;
  @Autowired
  private AuthenticationManager authenticationManager;

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome to Spring Security tutorials !!";
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDto> register(@RequestBody User user) throws ApplicationException {
    return new ResponseEntity<AuthResponseDto>(authService.register(user), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody AuthDto authDto) throws Exception {
    Authentication authenticate = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(),
            authDto.getPassword()));

    if (authenticate.isAuthenticated()) {
      return ResponseEntity.ok(authService.login(authDto));
    } else {
      throw new ApplicationException("401", "Not Authenticated",
          HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto)
      throws ApplicationException {
    ColorLogger.logError(refreshTokenDto.getToken());
    return ResponseEntity.ok(authService.getNewTokenByRefreshToken(refreshTokenDto));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<CommonResponseDto> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto,
      @RequestHeader(value = "Authorization") String jwtToken)
      throws ApplicationException {
    return ResponseEntity.ok(authService.forgotPassword(forgotPasswordDto, jwtToken));
  }
}