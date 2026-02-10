
package com.example.oauth2.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Generate Secret Key:
 * byte[] bytes = new byte[64];
 * Random random = new SecureRandom();
 * random.nextBytes(bytes);
 * String.valueOf(Hex.encode(bytes));
 */

@Service
public class JwtService {

    @Value("${application.security.jwt.signing-key}")
    private String signingKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username) {
        long expiration = 2;
        return generateToken(new HashMap<>(), username, expiration);
    }

    public String generateToken(Map<String, Object> extraClaims, String username, long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration * 60 * 1000))
                .signWith(createSignKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(createSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey createSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}