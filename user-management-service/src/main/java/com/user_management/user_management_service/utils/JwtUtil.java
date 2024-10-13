package com.user_management.user_management_service.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.token.expiration}")
    private long jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        logger.info("Generating JWT for email: {}", email);

        if (email == null || email.isEmpty()) {
            logger.error("Extracted email is null or empty.");
            throw new RuntimeException("Failed to generate JWT token: email is null or empty");
        }

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Build the JWT token
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecretKey));
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            logger.info("JWT token is valid.");
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token", e.getMessage());
        }
        return false;
    }

    public String getEmailFromJwt(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> getRolesFromJwt(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class); // Retrieve roles as a List
    }

    public String getUsernameFromJwt(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
