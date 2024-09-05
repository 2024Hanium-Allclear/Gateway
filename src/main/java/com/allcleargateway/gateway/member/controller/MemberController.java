package com.allcleargateway.gateway.member.controller;

import com.allcleargateway.gateway.auth.dto.LoginDto;
import com.allcleargateway.gateway.member.dto.SignupDto;
import com.allcleargateway.gateway.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController("api/member")
public class MemberController {

    final private MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginDto.ResponseDto> login(
            @RequestBody LoginDto.RequestDto loginDto) {
        return ResponseEntity.ok(memberService.login(loginDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody SignupDto.RequestDto signupDto){
        return ResponseEntity.ok(memberService.signUp(signupDto));
    }

}
