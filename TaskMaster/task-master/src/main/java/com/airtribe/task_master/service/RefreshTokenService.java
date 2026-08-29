package com.airtribe.task_master.service;

import com.airtribe.task_master.entity.RefreshToken;
import com.airtribe.task_master.entity.User;
import com.airtribe.task_master.repository.RefreshTokenRepository;
import com.airtribe.task_master.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(
        RefreshTokenRepository refreshTokenRepository,
        JwtService jwtService) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public RefreshToken createRefreshToken(
        User user, UserDetails userDetails) {

        String token = jwtService.generateRefreshToken(
            userDetails.getUsername()
        );

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(
            LocalDateTime.now().plusDays(7)
        );
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException(
                "Invalid refresh token"
            ));

        if (refreshToken.isRevoked()) {
            throw new IllegalArgumentException(
                "Refresh token has been revoked"
            );
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                "Refresh token has expired"
            );
        }

        return refreshToken;
    }

    @Transactional
    public RefreshToken rotateRefreshToken(
        RefreshToken oldToken,
        UserDetails userDetails) {

        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        return createRefreshToken(
            oldToken.getUser(),
            userDetails
        );
    }

    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
        });
    }
}
