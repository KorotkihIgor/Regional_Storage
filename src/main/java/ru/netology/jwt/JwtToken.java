package ru.netology.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtToken {
    @Value("${secret.key}")
    private String secretKey;

    @Value("${jwt.time.live}")
    private long jwtTimeLive;

    //    generate JWT token.
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date carrentDate = new Date();
        Date expirationDate = Date.from(LocalDateTime.now().plusMinutes(jwtTimeLive)
                .atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(carrentDate)
                .expiration(expirationDate)
                .signWith(key())
                .compact();
        return token;

    }

    //    validate JWT token.
    public boolean validateToken(String token) {
        Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parse(token);
        return true;
    }

    //    get username from JWT token.
    public String getUsernameToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
