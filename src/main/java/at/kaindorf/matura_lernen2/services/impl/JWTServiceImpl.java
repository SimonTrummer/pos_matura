package at.kaindorf.matura_lernen2.services.impl;

import at.kaindorf.matura_lernen2.pojos.TokenType;
import at.kaindorf.matura_lernen2.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {
    @Value("${app.verification-token.expiry-duration}")
    private Duration expiresIn;
    @Value("${token.signing.secret}")
    private String secret;

    @Override
    public String generateToken(UserDetails userDetails,TokenType tokenType) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .claim("type",tokenType.name())
                .expiration(new Date(System.currentTimeMillis() + expiresIn.toMillis()))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails, TokenType tokenType) {
        Claims claims = extractClaims(token);
        String username = userDetails.getUsername();
        Date actualDate = new Date();

        return username.equals(claims.getSubject()) && actualDate.before(claims.getExpiration()) && claims.get("type").equals(tokenType.name());
    }

    // Claims ist quasi HashMap - Meine Claims stehen da drin als Key-Value-Paare
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build() // baut den Parser
                .parseSignedClaims(token)
                .getPayload(); // holt die Claims raus
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private SecretKey getSigningKey() {
        byte [] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
