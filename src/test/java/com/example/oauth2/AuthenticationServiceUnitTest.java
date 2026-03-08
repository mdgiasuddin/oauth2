package com.example.oauth2;

import com.example.oauth2.config.JwtService;
import com.example.oauth2.model.dto.request.TokenRefreshRequest;
import com.example.oauth2.model.dto.response.TokenResponse;
import com.example.oauth2.model.entity.RefreshToken;
import com.example.oauth2.model.entity.User;
import com.example.oauth2.service.AuthenticationService;
import com.example.oauth2.service.RefreshTokenService;
import com.example.oauth2.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceUnitTest {

    @Mock
    private UserService userService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testRefreshAccessToken_Success() {
        // Arrange
        String oldRefreshTokenStr = UUID.randomUUID().toString() + "-oldkey";
        TokenRefreshRequest request = new TokenRefreshRequest(oldRefreshTokenStr);

        User user = new User();
        user.setUsername("testuser");

        UUID tokenId = UUID.randomUUID();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(tokenId);
        refreshToken.setRotationKey("newkey");
        refreshToken.setUser(user);

        when(refreshTokenService.validateToken(oldRefreshTokenStr)).thenReturn(refreshToken);
        when(jwtService.generateToken("testuser")).thenReturn("newAccessToken");

        // Act
        TokenResponse response = authenticationService.refreshAccessToken(request);

        // Assert
        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());
        assertEquals(tokenId.toString() + "-newkey", response.refreshToken());
    }
}
