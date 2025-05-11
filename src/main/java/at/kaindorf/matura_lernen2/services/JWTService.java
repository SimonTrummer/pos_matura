package at.kaindorf.matura_lernen2.services;

import org.springframework.security.core.userdetails.UserDetails;


public interface JWTService {
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    String extractUsername(String token);
}
