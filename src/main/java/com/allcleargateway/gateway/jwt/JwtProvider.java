package com.allcleargateway.gateway.jwt;

import com.allcleargateway.gateway.UserClient;
import com.allcleargateway.gateway.member.dto.StudentDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String JWT_SECRET ;
    UserClient userClient;




    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public StudentDto getAuthentication(String token) {
        Long memberId = this.getMemberIdFromJwtToken(token);

        //user보내 정보 얻어오기
        return userClient.forwardGetRequest(memberId);
    }


    // JWT 토큰 으로부터 memberId 추출
    public Long getMemberIdFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                            StandardCharsets.UTF_8)))
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch(Exception e) {
            throw new JwtException(e.getMessage());
        }
    }

    // 토큰 유효성 + 만료일자 확인 (만료 여부만 확인. 에러 발생 x)
    public Boolean validateTokenBoolean(String token) {
        Date now = new Date();

        try{
            // 주어진 토큰을 파싱하고 검증.
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(StandardCharsets.UTF_8)))
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date(now.getTime()));
        }catch (JwtException e){

            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    // Request Header에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
