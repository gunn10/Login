package com.example.SpringOuth.config;


import com.example.SpringOuth.Model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtils {

    private String SECRET_KEY = "";

    public JWTUtils() throws NoSuchAlgorithmException {
     KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacSHA256");
     SecretKey sk =keyGenerator.generateKey();

    SECRET_KEY= Base64.getEncoder().encodeToString(sk.getEncoded());
 }
    public String generateToken(User user) {
        long EXPIRATION_TIME = 86400000;
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole())  // Include user's role in the token
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getkey())
                .compact();
    }
    private Key getkey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

}
