package com.example.pricetag.services;

import com.example.pricetag.dto.RefreshTokenDto;
import com.example.pricetag.entity.RefreshToken;
import com.example.pricetag.entity.User;
import com.example.pricetag.exceptions.ApplicationException;
import com.example.pricetag.repository.RefreshTokenRepo;
import com.example.pricetag.responses.AuthResponseDto;
import com.example.pricetag.utils.ColorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepo refreshTokenRepo;

    @Autowired
    public TokenService(JwtService jwtService, RefreshTokenService refreshTokenService, RefreshTokenRepo refreshTokenRepo) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    public AuthResponseDto createAuthResponse(String email, String message) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);
        String jwtToken = jwtService.generateToken(email);
        
        return AuthResponseDto.builder()
                .success(true)
                .message(message)
                .data(AuthResponseDto.AuthResponseDataDto.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken.getToken())
                        .build())
                .build();
    }

    public AuthResponseDto refreshToken(RefreshTokenDto refreshTokenDto) throws ApplicationException {
        return refreshTokenService.findByToken(refreshTokenDto.getToken())
                .map(refreshTokenService::verifyRefreshTokenExpirationDate)
                .map(RefreshToken::getUser)
                .map(user -> {
                    ColorLogger.logInfo("refreshToken :: user: " + user);
                    String accessToken = jwtService.generateToken(user.getEmail());
                    return AuthResponseDto.builder()
                            .success(true)
                            .message("Token refreshed successfully")
                            .data(AuthResponseDto.AuthResponseDataDto.builder().accessToken(accessToken).build())
                            .build();
                })
                .orElseThrow(() -> new ApplicationException(null, "Refresh Token not found", HttpStatus.BAD_REQUEST));
    }

    public void cleanupExistingRefreshTokens(User user) {
        Optional<RefreshToken> existingRefreshToken = refreshTokenService.getRefreshTokenByUser(user);
        existingRefreshToken.ifPresent(token -> refreshTokenRepo.delete(token));
    }
}
