package ru.netology.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.netology.model.Person;

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
    public String generateToken(Person person) {
        String username = person.getUsername();
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
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Срок действия JWT истёк!");
            e.getMessage();
        } catch (MalformedJwtException e) {
            System.out.println("Форма JWT некорректна!");
            e.getMessage();
        } catch (SignatureException e) {
            System.out.println("Недействительная подпись!");
            e.getMessage();
        }
        return false;
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
