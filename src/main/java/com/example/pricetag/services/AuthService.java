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
import com.example.pricetag.entity.RefreshToken;
import com.example.pricetag.entity.User;
import com.example.pricetag.enums.AppUserRole;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.AuthRepository;
import com.example.pricetag.responses.AuthResponseDto;
import com.example.pricetag.utils.ColorLogger;

@Service
public class AuthService implements UserDetailsService {
  @Autowired
  private AuthRepository authRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private RefreshTokenService refreshTokenService;
  @Autowired
  private JwtService jwtService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = authRepository.findByEmail(username);
    return user.map(AuthDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
  }

  public AuthResponseDto register(User user) throws ApplicationException {
    ColorLogger
        .logInfo(this.authRepository.findByEmail(user.getEmail()).toString());
    Optional<User> existingUser = this.authRepository.findByEmail(user.getEmail());
    if (!existingUser.isPresent()) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setAppUserRole(AppUserRole.USER);
      authRepository.save(user);
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

  // public AuthResponseDto login(AuthDto authDto) throws ApplicationException {
  // Authentication authenticate = authenticationManager
  // .authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(),
  // authDto.getPassword()));

  // if (authenticate.isAuthenticated()) {
  // var user = this.userRepo.findByEmail(authDto.getEmail())
  // .orElseThrow(() -> new ApplicationException("404", "Email not found",
  // HttpStatus.NOT_FOUND));
  // ColorLogger.logError(user.toString());

  // Optional<RefreshToken> existingRefreshToken = refreshTokenService
  // .getRefreshTokenByUser(user);
  // existingRefreshToken.ifPresent(token ->
  // refreshTokenRepo.delete(existingRefreshToken.get()));

  // RefreshToken refreshToken = refreshTokenService
  // .createRefreshToken(authDto.getEmail());

  // AuthResponseDto authResponseDto = AuthResponseDto
  // .builder()
  // .accessToken(jwtService.generateToken(authDto.getEmail()))
  // .refreshToken(refreshToken.getToken())
  // .build();
  // return authResponseDto;
  // } else {
  // throw new ApplicationException("401", "Not Authenticated",
  // HttpStatus.UNAUTHORIZED);
  // }
  // }

  // public List<User> getAllUser() {
  // return authRepository.findAll();
  // }

  // public User getUser(Integer id) {
  // return authRepository.findById(id).get();
  // }
}