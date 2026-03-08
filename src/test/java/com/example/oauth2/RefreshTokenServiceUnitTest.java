package com.example.oauth2;

import com.example.oauth2.model.entity.RefreshToken;
import com.example.oauth2.repository.RefreshTokenRepository;
import com.example.oauth2.service.RefreshTokenService;
import com.example.oauth2.util.RandomStringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceUnitTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private RandomStringUtil randomStringUtil;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void testValidateToken_Success() {
        // Arrange
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenValidity", 7L);
        UUID id = UUID.randomUUID();
        String oldRotationKey = "oldkey";
        String tokenStr = id.toString() + "-" + oldRotationKey;

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(id);
        refreshToken.setRotationKey(oldRotationKey);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(1));

        when(refreshTokenRepository.findById(id)).thenReturn(Optional.of(refreshToken));
        when(randomStringUtil.randomAlphanumericLower(10)).thenReturn("newkey");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RefreshToken result = refreshTokenService.validateToken(tokenStr);

        // Assert
        assertNotNull(result);
        assertEquals("newkey", result.getRotationKey());
    }

    @Test
    void testValidateToken_InvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.validateToken("invalid-token"));
        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.validateToken("notuid-key"));
    }
}
