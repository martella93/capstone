package it.epicode.capstone.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.duration}")
    private Long duration;

    public String createToken(User user){
        return Jwts.builder().issuedAt(new Date(System.currentTimeMillis())).
                expiration(new Date((System.currentTimeMillis()+ duration))).
                subject(String.valueOf(user.getId())).
                signWith(Keys.hmacShaKeyFor(secret.getBytes())).
                compact();
    }

    public void verifyToken(String token){
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        }
        catch (Exception e){
            throw new UnauthorizedException("Error in authorizated, relogin");
        }
    }

    public int getIdFromToken(String token){
        return Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token).getPayload().getSubject());
    }
}
