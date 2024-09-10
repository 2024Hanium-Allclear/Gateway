package com.allcleargateway.gateway.domain.member.controller;

import com.allcleargateway.gateway.domain.auth.dto.LoginDto;
import com.allcleargateway.gateway.domain.member.dto.SignupDto;
import com.allcleargateway.gateway.domain.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/member")
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
