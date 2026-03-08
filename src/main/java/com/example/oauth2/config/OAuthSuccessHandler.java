package com.example.oauth2.config;

import com.example.oauth2.model.entity.User;
import com.example.oauth2.service.RefreshTokenService;
import com.example.oauth2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${application.security.ui-redirect-url}")
    private String uiRedirectUrl;

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        assert oauthUser != null;
        User user = storeUserToDb(oauthUser);

        String accessToken = jwtService.generateToken(user.getUsername());
        String refreshToken = refreshTokenService.generateNewToken(user);

        response.sendRedirect(
                String.format("%s?access_token=%s&refresh_token=%s", uiRedirectUrl, accessToken, refreshToken)
        );
    }

    private User storeUserToDb(OAuth2User oauthUser) {
        String username = oauthUser.getAttribute("email") == null ? oauthUser.getAttribute("login")
                : oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        User user = userService.saveOauthUser(username, name);

        log.info("User login via Oauth: {}", username);
        return user;
    }
}

