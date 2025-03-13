package com.example.CollegeBackend.utils;

import com.example.CollegeBackend.dto.JwtPayload;
import com.example.CollegeBackend.dto.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET = "Sd6ac0PU/L+VG9WUFxwqS/iDpCHg1Ak2S4ndVhEN/HQ=";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));


    public static String generateToken(JwtPayload payload) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", payload.getEmail());
        claims.put("role", payload.getRole().name());


        return Jwts.builder().claims(claims).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 10))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static JwtPayload parseToken(String token) {
        try{
            JwtParser parser = Jwts.parser().verifyWith((SecretKey) SECRET_KEY).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();
            String email = claims.get("email",String.class);
            String roleString = claims.get("role", String.class);
            Long id = claims.get("id", Long.class);
            Role role = Role.valueOf(roleString);
            return new JwtPayload(email, role,id);
        }catch (Exception e){
            return null;
        }

    }
}
