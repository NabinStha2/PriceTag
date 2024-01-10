package com.example.pricetag.services.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.pricetag.entity.RefreshToken;
import com.example.pricetag.entity.User;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.AuthRepository;
import com.example.pricetag.repository.RefreshTokenRepo;
import com.example.pricetag.services.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
  @Autowired
  private AuthRepository authRepository;
  @Autowired
  private RefreshTokenRepo refreshTokenRepo;

  @Override
  public RefreshToken createRefreshToken(String email) {
    RefreshToken refreshToken = RefreshToken
        .builder()
        .user(this.authRepository.findByEmail(email).get())
        .token(UUID.randomUUID().toString())
        .expiryDate(Instant.now().plusMillis(600000)) // 10 minute
        .build();

    return refreshTokenRepo.save(refreshToken);

  }

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepo.findByToken(token);
  }

  @Override
  public RefreshToken verifyRefreshTokenExpirationDate(RefreshToken refreshToken) {
    if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepo.delete(refreshToken);
      throw new ApplicationException("400", "Refresh Token already expired. Please Sign In to continue.",
          HttpStatus.BAD_REQUEST);
    }

    return refreshToken;
  }

  @Override
  public Optional<RefreshToken> getRefreshTokenByUser(User user) {
    return refreshTokenRepo.findByUser(user);
  }

}
