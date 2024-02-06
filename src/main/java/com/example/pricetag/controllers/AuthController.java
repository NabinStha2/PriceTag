package com.example.pricetag.controllers;

import com.example.pricetag.dto.*;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.responses.AuthResponseDto;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.services.AuthService;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
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
    public ResponseEntity<CommonResponseDto> register(@RequestBody RegisterUserDto registerUserDto) throws ApplicationException {
        return new ResponseEntity<CommonResponseDto>(authService.register(registerUserDto), HttpStatus.CREATED);
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
            throw new ApplicationException("400", e.getMessage(),
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