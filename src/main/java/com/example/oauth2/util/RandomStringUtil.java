package com.example.oauth2.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class RandomStringUtil {
    private final Random random;

    public final String alphanumericLower = "abcdefghijklmnopqrstuvwxyz0123456789";

    public String randomAlphanumericLower(int len) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            str.append(alphanumericLower.charAt(random.nextInt(alphanumericLower.length())));
        }

        return str.toString();
    }
}
