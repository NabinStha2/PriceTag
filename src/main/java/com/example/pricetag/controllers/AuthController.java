package com.example.pricetag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricetag.dto.AuthDto;
import com.example.pricetag.dto.ForgotPasswordDto;
import com.example.pricetag.dto.OtpDto;
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
  public ResponseEntity<CommonResponseDto> register(@RequestBody User user) throws ApplicationException {
    return new ResponseEntity<CommonResponseDto>(authService.register(user), HttpStatus.CREATED);
  }

  @PostMapping("/verify-otp")
  public ResponseEntity<AuthResponseDto> verifyOtp(@RequestBody OtpDto otpDto) throws ApplicationException {
    return new ResponseEntity<AuthResponseDto>(authService.verifyOtp(otpDto), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody AuthDto authDto) throws Exception {
    try {
      Authentication authenticate = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(),
              authDto.getPassword()));

      if (authenticate.isAuthenticated()) {
        return ResponseEntity.ok(authService.login(authDto));
      } else {
        throw new ApplicationException("401", "Not Authenticated",
            HttpStatus.UNAUTHORIZED);
      }
    } catch (AuthenticationException e) {
      throw new ApplicationException("400", "Password incorrect",
          HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto)
      throws ApplicationException {
    ColorLogger.logError(refreshTokenDto.getToken());
    return ResponseEntity.ok(authService.getNewTokenByRefreshToken(refreshTokenDto));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<CommonResponseDto> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto)
      throws ApplicationException {
    return ResponseEntity.ok(authService.forgotPassword(forgotPasswordDto));
  }

  @PatchMapping("/verify-forgot-password-otp")
  public ResponseEntity<CommonResponseDto> verifyForgotPasswordOtp(@RequestBody OtpDto otpDto)
      throws ApplicationException {
    return ResponseEntity.ok(authService.verifyForgotPasswordOtp(otpDto));
  }
}