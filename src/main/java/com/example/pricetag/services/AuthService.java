package com.example.pricetag.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pricetag.config.AuthDetails;
import com.example.pricetag.dto.AuthDto;
import com.example.pricetag.dto.ForgotPasswordDto;
import com.example.pricetag.dto.RefreshTokenDto;
import com.example.pricetag.entity.RefreshToken;
import com.example.pricetag.entity.User;
import com.example.pricetag.enums.AppUserRole;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.AuthRepository;
import com.example.pricetag.repository.RefreshTokenRepo;
import com.example.pricetag.repository.UserRepo;
import com.example.pricetag.responses.AuthResponseDto;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.utils.ColorLogger;

@Service
public class AuthService implements UserDetailsService {
  @Autowired
  private AuthRepository authRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private RefreshTokenService refreshTokenService;
  @Autowired
  private RefreshTokenRepo refreshTokenRepo;
  @Autowired
  private UserRepo userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    ColorLogger.logInfo("I am inside AuthService loadUserByUsername");
    Optional<User> user = authRepository.findByEmail(username);
    return user.map(AuthDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
  }

  public AuthResponseDto register(User user) throws ApplicationException {
    ColorLogger
        .logInfo(this.authRepository.findByEmail(user.getEmail()).toString());
    Optional<User> existingUser = this.authRepository.findByEmail(user.getEmail());
    if (!existingUser.isPresent()) {
      
      User newUser = new User();
      newUser.setEmail(user.getEmail());
      newUser.setName(user.getName());
      newUser.setPhoneNumber(user.getPhoneNumber());
      newUser.setPassword(passwordEncoder.encode(user.getPassword()));
      newUser.setAppUserRole(AppUserRole.ROLE_USER);
      authRepository.save(newUser);
      RefreshToken refreshToken = refreshTokenService
          .createRefreshToken(user.getEmail());
      var jwtToken = jwtService.generateToken(user.getEmail());
      return AuthResponseDto
          .builder()
          .accessToken(jwtToken)
          .refreshToken(refreshToken.getToken())
          .build();
    } else {
      throw new ApplicationException("400", "Email already exists", HttpStatus.BAD_REQUEST);
    }
  }

  public AuthResponseDto getNewTokenByRefreshToken(RefreshTokenDto refreshTokenDto) throws ApplicationException {
    return refreshTokenService
        .findByToken(refreshTokenDto.getToken())
        .map(refreshTokenService::verifyRefreshTokenExpirationDate)
        .map(RefreshToken::getUser)
        .map(user -> {
          ColorLogger.logInfo("refreshToken :: user: " + user);
          String accessToken = jwtService.generateToken(user.getEmail());
          return AuthResponseDto
              .builder()
              .accessToken(accessToken)
              .build();
        }).orElseThrow(() -> new ApplicationException(null, "Refresh Token not found", HttpStatus.BAD_REQUEST));
  }

  public AuthResponseDto login(AuthDto authDto) throws ApplicationException {

    var user = this.userRepo.findByEmail(authDto.getEmail())
        .orElseThrow(() -> new ApplicationException("404", "Email not found",
            HttpStatus.NOT_FOUND));
    ColorLogger.logError(user.toString());

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
  }

  public CommonResponseDto forgotPassword(ForgotPasswordDto forgotPasswordDto, String jwtToken)
      throws ApplicationException {
    try {
      String email = jwtService.extractUserName(jwtToken);
      if (email != null) {
        userRepo.findByEmail(email)
            .orElseThrow(() -> new ApplicationException("404", "Email not found",
                HttpStatus.NOT_FOUND));

        User user = new User();
        user.setPassword(forgotPasswordDto.getNewPassword());
        userRepo.save(user);
        return CommonResponseDto
            .builder()
            .message("password updated successfully")
            .statusCode("200")
            .build();

      }
    } catch (Exception e) {
      ColorLogger.logError("I am inside forgotPassword Error :: " + e.getMessage());
    }
    return null;

  }
}