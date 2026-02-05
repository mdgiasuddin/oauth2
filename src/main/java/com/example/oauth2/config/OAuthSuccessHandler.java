package com.example.oauth2.config;

import com.example.oauth2.UserRepository;
import com.example.oauth2.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        assert oauthUser != null;
        String username = storeUserToDb(oauthUser);

        String token = jwtService.generateToken(username);

        response.sendRedirect(
                "http://frontend-app.com/oauth-success?token=" + token
        );
    }

    private String storeUserToDb(OAuth2User oauthUser) {
        String username = oauthUser.getAttribute("email") == null ? oauthUser.getAttribute("login")
                : oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty() || !userOptional.get().getName().equals(name)) {
            User user = new User();
            user.setName(name);
            user.setUsername(username);
            userRepository.save(user);
        }

        log.info("User login via Oauth: {}", username);
        return username;
    }
}

