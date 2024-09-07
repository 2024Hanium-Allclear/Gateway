package com.allcleargateway.gateway.auth.service;

import com.allcleargateway.gateway.UserClient;
import com.allcleargateway.gateway.auth.dto.LoginDto;
import com.allcleargateway.gateway.auth.dto.TokenDto;
import com.allcleargateway.gateway.jwt.JwtGenerator;
import com.allcleargateway.gateway.jwt.JwtProvider;
import com.allcleargateway.gateway.member.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    final private JwtGenerator jwtGenerator;
    final private JwtProvider jwtProvider;
    final private UserClient userClient;

    public TokenDto.GetAccessTokenResponseDto generateAccessToken(String token) {
        if(!jwtProvider.validateTokenBoolean(token))
            throw new RuntimeException("토큰깂이 없습니다.");
        Long memberId = jwtProvider.getMemberIdFromJwtToken(token);
        String accessToken = jwtGenerator.generateAccessToken(memberId);
        return TokenDto.GetAccessTokenResponseDto.builder()
                .accessToken(accessToken).build();
    }

    public LoginDto.ResponseDto generateToken(StudentDto studentDto){
        String newAccessToken = jwtGenerator.generateAccessToken(studentDto.getId());
        String newRefreshToken = jwtGenerator.generateRefreshToken(studentDto.getId());

        TokenDto.UpdateRefreshTokenRequestDto loginRequestDto = TokenDto.UpdateRefreshTokenRequestDto.builder()
                .id(studentDto.getId())
                .refreshToken(newRefreshToken).build();
        //새로받은 refresh 토큰 변경
        userClient.forwardRefreshTokenPostRequest(loginRequestDto);

        return LoginDto.ResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}
