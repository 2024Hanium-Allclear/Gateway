package com.allcleargateway.gateway.domain.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtGenerator {

    @Value("${jwt.secret}")
    private String JWT_SECRET;


    private Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60; // 1h
    private Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7; // 7d



    public String generateAccessToken(Long userId){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("allclear")
                .setIssuedAt(now)
                .setSubject(userId.toString())
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .claim("memberId",userId)
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }
    public String generateRefreshToken(Long userId){
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setSubject(userId.toString())
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .claim("memberId", userId)
                .claim("roles", "USER")
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }

}
