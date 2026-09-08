package com.airtribe.task_master.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
        @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {

        this.secretKey = Keys.hmacShaKeyFor(
            secret.getBytes(StandardCharsets.UTF_8)
        );

        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(String username) {
        return generateToken(
            username,
            accessTokenExpiration
        );
    }
    
    public String generateRefreshToken(String username) {
        return generateToken(
            username,
            refreshTokenExpiration
        );
    }

    private String generateToken(String username, long expiration) {
        Date now = new Date();
        Date expiry = new Date(
            now.getTime() + expiration
        );

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(
            token,
            Claims::getSubject
        );
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
            && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
            .before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(
            token,
            Claims::getExpiration
        );
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {

        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return resolver.apply(claims);
    }
}
