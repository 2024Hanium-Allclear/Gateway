package com.allcleargateway.gateway.domain.jwt;

import com.allcleargateway.gateway.domain.member.dto.StudentDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private JwtProvider jwtProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider =jwtProvider;

    }

    @Override
    public void  doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 1. Request Header에서 JWT 토큰 추출
        String token = jwtProvider.resolveToken((HttpServletRequest) request);
        // 2. validateToken으로 토큰 유효성 검사
        if (token != null && jwtProvider.validateTokenBoolean(token)) {
            // 토큰이 유효할 경우 user에 접근해 정보 가져오기

            StudentDto studentDto = jwtProvider.getAuthentication(token);
            request.setAttribute("student", studentDto);

        }
        else if (StringUtils.isEmpty(token)){ // 널일때
            throw new NullPointerException();
        }

        // 에러 핸들링 필요
        chain.doFilter(request, response);
    }

}
