package com.lab.dev.shawn.api.util;

import com.lab.dev.shawn.api.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private final static Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private final static long expirationMs = 86400000; // 令牌过期时间（24小时）

    public static String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("roles", user.getRoles())
                .claim("accountId", user.getAccountId())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public static Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
