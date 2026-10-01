package com.example.prueba.security;

import com.example.prueba.user.domain.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }

    /** Email (subject) of a valid token; empty if the token is malformed, tampered or expired. */
    public Optional<String> extractEmail(String token) {
        try {
            return Optional.ofNullable(Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token).getPayload().getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
