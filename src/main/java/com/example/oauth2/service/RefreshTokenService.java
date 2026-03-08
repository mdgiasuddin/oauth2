package com.example.oauth2.service;

import com.example.oauth2.model.entity.RefreshToken;
import com.example.oauth2.model.entity.User;
import com.example.oauth2.repository.RefreshTokenRepository;
import com.example.oauth2.util.RandomStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RandomStringUtil randomStringUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${application.security.refresh-token.validity-day}")
    private long refreshTokenValidity;

    private final int rotationKeyLen = 10;

    public String generateNewToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRotationKey(randomStringUtil.randomAlphanumericLower(rotationKeyLen));
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshTokenValidity));
        refreshToken = refreshTokenRepository.save(refreshToken);

        return String.format("%s-%s", refreshToken.getId().toString(), refreshToken.getRotationKey());
    }

    public RefreshToken validateToken(String token) {
        UUID id = UUID.fromString(token.substring(0, token.length() - (rotationKeyLen + 1)));
        String rotationKey = token.substring(token.length() - rotationKeyLen);

        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenById(id)
                .orElseThrow(() -> {
                    log.warn("Refresh not found for id: {}", id);
                    return new IllegalArgumentException("Refresh token not found for id: " + id);
                });

        if (!refreshToken.getRotationKey().equals(rotationKey) || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.info("Refresh: {}, {}, {}", refreshToken.getRotationKey(), rotationKey, refreshToken.getExpiryDate());
            throw new IllegalArgumentException("Refresh token is  not valid or expired");
        }

        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshTokenValidity));
        refreshToken.setRotationKey(randomStringUtil.randomAlphanumericLower(rotationKeyLen));
        return refreshTokenRepository.save(refreshToken);
    }
}