package com.allcleargateway.gateway.domain.member.service;

import com.allcleargateway.gateway.global.client.UserClient;
import com.allcleargateway.gateway.domain.auth.dto.LoginDto;
import com.allcleargateway.gateway.domain.auth.service.AuthService;
import com.allcleargateway.gateway.domain.member.dto.SignupDto;
import com.allcleargateway.gateway.domain.member.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class MemberService {
    private final AuthService authService;
    private final UserClient userClient;
    public LoginDto.ResponseDto login(LoginDto.RequestDto loginDto) {
        StudentDto studentDto = userClient.forwardLoginPostRequest(loginDto).getBody();
        return authService.generateToken(studentDto);
    }

    public Boolean signUp(SignupDto.RequestDto signupDto) {
        return userClient.forwardSignUpPostRequest(signupDto).getBody();
    }
}
