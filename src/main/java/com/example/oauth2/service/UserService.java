package com.example.oauth2.service;

import com.example.oauth2.model.dto.request.UserRegistrationRequest;
import com.example.oauth2.model.entity.User;
import com.example.oauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User saveOauthUser(String username, String name) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty() || !userOptional.get().getName().equals(name)) {
            User user = new User();
            user.setName(name);
            user.setUsername(username);
            return userRepository.save(user);
        }

        return userOptional.get();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow();
    }

    public void registerUser(UserRegistrationRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }
}
