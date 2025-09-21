package com.shafay.SpringSecurity.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${app.security.jwt.secret-key}")
  private String SECRET_KEY;
  @Value("${app.security.jwt.expiration-time}")
  private long EXPIRATION_TIME;

  // Generate token
  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  // Extract username
  public String extractUsername(String token) {
    return parseClaims(token).getSubject();
  }

  // Validate token
  public boolean validateToken(String token, String username) {
    return (username.equals(extractUsername(token)) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return parseClaims(token).getExpiration().before(new Date());
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getKey()).build()
        .parseClaimsJws(token).getBody();
  }

  private Key getKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }
}