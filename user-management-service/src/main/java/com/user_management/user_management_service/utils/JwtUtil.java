package com.user_management.user_management_service.utils;

import com.user_management.user_management_service.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtUtil(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Generates JWT token based on authentication information
    public String generateJwtToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info("UserDetails type: {}", userDetails.getClass().getName());

        String email = userDetails.getUsername();
        String userId = getUserIdByEmail(email); // Use the new method to get user ID

        logger.info("Generating JWT for email: {}", email);

        if (email == null || email.isEmpty()) {
            logger.error("Extracted email is null or empty.");
            throw new RuntimeException("Failed to generate JWT token: email is null or empty");
        }

        // Extract roles from the authentication object
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Build the JWT token

        // existing code...

        String jwtToken = Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        logger.info("Generated JWT Token: {}", jwtToken); // Log the generated token

        return jwtToken;
    }

    // New method to get userId from email
    private String getUserIdByEmail(String email) {
        return String.valueOf(userDetailsService.getUserIdByEmail(email)); // Convert to String if necessary
    }

    // Extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }

    // Extract specific claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check if the token is expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate the signing key
    private Key key() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecretKey));
    }

    // Validate the JWT token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            logger.info("JWT token is valid.");
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token", e);
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature", e);
        } catch (Exception e) {
            logger.error("JWT token validation failed", e);
        }
        return false;
    }

    // Get email from the JWT token
    public String getEmailFromJwt(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Get roles from the JWT token
    public List<String> getRolesFromJwt(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class); // Retrieve roles as a List
    }

    // Get username from the JWT token
    public String getUsernameFromJwt(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Get userId from the JWT token
    public String getUserIdFromJwt(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", String.class);
    }

    // Helper method to extract user ID from UserDetails
    private String getUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof UserDetailsServiceImpl.CustomUserDetails) {
            return String.valueOf(((UserDetailsServiceImpl.CustomUserDetails) userDetails).getId());
        }
        logger.error("UserDetails does not contain user ID.");
        throw new RuntimeException("UserDetails does not contain user ID.");
    }
}
