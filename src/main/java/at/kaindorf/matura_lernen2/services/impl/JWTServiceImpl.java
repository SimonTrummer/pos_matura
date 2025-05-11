package at.kaindorf.matura_lernen2.services.impl;

import at.kaindorf.matura_lernen2.services.JWTService;
import ch.qos.logback.core.util.SystemInfo;
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
    @Value("${token.signing.secret}")
    private String secret;
    @Value("${app.verification-token.expiry-duration}")
    private Duration duration;

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .issuedAt(new Date())
                .subject(userDetails.getUsername())
                .expiration(new Date(System.currentTimeMillis() + duration.toMillis()))
                .signWith(getSigningKey())
                .claim("jwt","true")
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractClaims(token);
        String username = userDetails.getUsername();
        Date actualDate = new Date();
        return username.equals(claims.getSubject()) && actualDate.before(claims.getExpiration());
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
