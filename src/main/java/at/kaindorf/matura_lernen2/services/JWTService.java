package at.kaindorf.matura_lernen2.services;

import at.kaindorf.matura_lernen2.pojos.TokenType;
import org.springframework.security.core.userdetails.UserDetails;


public interface JWTService {
    String generateToken(UserDetails userDetails, TokenType tokenType);
    boolean isTokenValid(String token, UserDetails userDetails, TokenType tokenType);
    String extractUsername(String token);
}
