package com.example.IAM.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;


public class JwtHandler {
    private static final String TOKEN_SECRET_KEY = "ThisIsAFakeSecretItShouldBeStoredInAConfigurationFileInsteadOfAConstantYouMustMoveItToConfigFile";
    private static final Long TOKEN_TTL = 1000 * 60 * 60 * 10L; // 10 hours

    public static String generateToken(String userId) {
        Date now = new Date(System.currentTimeMillis());
        Key secretKey = Keys.hmacShaKeyFor(TOKEN_SECRET_KEY.getBytes());
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_TTL))
                .signWith(secretKey)
                .compact();
    }

    public static boolean isValid(String jwt) {
        try {
            Key secretKey = Keys.hmacShaKeyFor(TOKEN_SECRET_KEY.getBytes());
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build();
            parser.parse(jwt);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    public static String getUserId(String jwtStr) {
        Key secretKey = Keys.hmacShaKeyFor(TOKEN_SECRET_KEY.getBytes());
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
        return parser.parseClaimsJws(jwtStr)
                .getBody()
                .getSubject();
    }

}
