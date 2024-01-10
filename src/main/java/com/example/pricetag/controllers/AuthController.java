package com.example.pricetag.controllers;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricetag.dto.AuthDto;
import com.example.pricetag.dto.RefreshTokenDto;
import com.example.pricetag.entity.RefreshToken;
import com.example.pricetag.entity.User;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.RefreshTokenRepo;
import com.example.pricetag.repository.UserRepo;
import com.example.pricetag.responses.AuthResponseDto;
import com.example.pricetag.services.AuthService;
import com.example.pricetag.services.JwtService;
import com.example.pricetag.services.RefreshTokenService;
import com.example.pricetag.utils.ColorLogger;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthService authService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private RefreshTokenService refreshTokenService;
  @Autowired
  private RefreshTokenRepo refreshTokenRepo;
  @Autowired
  private UserRepo userRepo;

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome to Spring Security tutorials !!";
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDto> register(@RequestBody User user) throws ApplicationException {
    return new ResponseEntity<AuthResponseDto>(authService.register(user), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public AuthResponseDto login(@RequestBody AuthDto authDto) throws Exception {
    Authentication authenticate = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));

    if (authenticate.isAuthenticated()) {
      var user = this.userRepo.findByEmail(authDto.getEmail())
          .orElseThrow(() -> new ApplicationException("404", "Email not found", HttpStatus.NOT_FOUND));
      // ColorLogger.logError(user.toString());

      Optional<RefreshToken> existingRefreshToken = refreshTokenService
          .getRefreshTokenByUser(user);
      existingRefreshToken.ifPresent(token -> refreshTokenRepo.delete(existingRefreshToken.get()));

      RefreshToken refreshToken = refreshTokenService
          .createRefreshToken(authDto.getEmail());

      AuthResponseDto authResponseDto = AuthResponseDto
          .builder()
          .accessToken(jwtService.generateToken(authDto.getEmail()))
          .refreshToken(refreshToken.getToken())
          .build();
      return authResponseDto;
    } else {
      throw new ApplicationException("400", "Not Authenticated", HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto)
      throws ApplicationException {
    ColorLogger.logError(refreshTokenDto.getToken());
    return refreshTokenService
        .findByToken(refreshTokenDto.getToken())
        .map(refreshTokenService::verifyRefreshTokenExpirationDate)
        .map(RefreshToken::getUser)
        .map(user -> {
          ColorLogger.logInfo("refreshToken :: user: " + user);
          String accessToken = jwtService.generateToken(user.getEmail());
          return ResponseEntity.ok(AuthResponseDto
              .builder()
              .accessToken(accessToken)
              .build());
        }).orElseThrow(() -> new ApplicationException(null, "Refresh Token not found", HttpStatus.BAD_REQUEST));

  }

  // @GetMapping("/getUsers")
  // @PreAuthorize("hasAuthority('USER')")
  // public List<User> getAllUsers() throws ApplicationException {
  // return authService.getAllUser();
  // }

  // @GetMapping("/getUsers/{id}")
  // @PreAuthorize("hasAuthority('USER')")
  // public User getAllUsers(@PathVariable Integer id) throws ApplicationException
  // {
  // return authService.getUser(id);
  // }
}