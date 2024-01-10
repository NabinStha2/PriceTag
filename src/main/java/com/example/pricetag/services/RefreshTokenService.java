package com.example.pricetag.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.pricetag.entity.RefreshToken;
import com.example.pricetag.entity.User;

@Service
public interface RefreshTokenService {

  public RefreshToken createRefreshToken(String email);

  public Optional<RefreshToken> findByToken(String token);

  public Optional<RefreshToken> getRefreshTokenByUser(User user);

  public RefreshToken verifyRefreshTokenExpirationDate(RefreshToken refreshToken);

}
