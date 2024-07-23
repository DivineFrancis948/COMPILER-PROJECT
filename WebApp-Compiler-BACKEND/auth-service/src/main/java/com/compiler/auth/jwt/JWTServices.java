package com.compiler.auth.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.compiler.auth.dto.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTServices 
{
	private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    // Generates a JWT token for a given UserDetails object
    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(userDetails, claims);
    }

    // Creates a JWT token with the specified claims, subject, issuedAt, expiration, and signing algorithm
    private String createToken(CustomUserDetails customUser, Map<String, Object> additionalClaims) {
        return Jwts.builder()
                .setSubject(customUser.getEmail())  // Set the subject claim
                .claim("customUser", customUser)  // Add custom class as a claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    
    // Validates a JWT token against a UserDetails object
    public boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }
    

    // Extracts the username from a JWT token
    public String extractUsername(String token) {
    	String username=extractClaim(token, Claims::getSubject);
    	System.out.println(username);
        return extractClaim(token, Claims::getSubject);
    }

    // Extracts a specific claim from a JWT token using a custom claimsResolver function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parses and extracts all claims from a JWT token
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // Checks if a JWT token is expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extracts the expiration date from a JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}

