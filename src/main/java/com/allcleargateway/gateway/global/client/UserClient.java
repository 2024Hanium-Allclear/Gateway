package com.allcleargateway.gateway.global.client;

import com.allcleargateway.gateway.domain.auth.dto.LoginDto;
import com.allcleargateway.gateway.domain.auth.dto.TokenDto;
import com.allcleargateway.gateway.global.config.FeignConfig;
import com.allcleargateway.gateway.domain.member.dto.SignupDto;
import com.allcleargateway.gateway.domain.member.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "userClient", url = "https://user.allclear-server.com:8082", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardGetRequest(
            @RequestHeader Map<String, String> headers,
            @PathVariable("relativeUrl") String relativeUrl
    );

    @PostMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardPostRequest(
            @RequestHeader Map<String, String> headers,
            @RequestBody String body,
            @PathVariable("relativeUrl") String relativeUrl
    );

    @PutMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardPutRequest(
            @RequestHeader Map<String, String> headers,
            @RequestBody String body,
            @PathVariable("relativeUrl") String relativeUrl
    );

    @DeleteMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardDeleteRequest(
            @RequestHeader Map<String, String> headers,
            @RequestBody(required = false) String body, // DELETE에서도 body 허용
            @PathVariable("relativeUrl") String relativeUrl
    );

    @PatchMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardPatchRequest(
            @RequestHeader Map<String, String> headers,
            @RequestBody String body,
            @PathVariable("relativeUrl") String relativeUrl
    );

    //login을 위해 DTO와 DB에 있는 엔티티 비교
    @PostMapping("/login")
    ResponseEntity<StudentDto> forwardLoginPostRequest(
            @RequestBody LoginDto.RequestDto body
    );
    
    @PostMapping("/signup")
    ResponseEntity<Boolean> forwardSignUpPostRequest(
            @RequestBody SignupDto.RequestDto body
    );

    //로그인시 update된 refreshToken 저장
    @PostMapping("/refreshToken")
    ResponseEntity<Boolean> forwardRefreshTokenPostRequest(
            @RequestBody TokenDto.UpdateRefreshTokenRequestDto body
    );

    @GetMapping("/student/info/{memberId}")
    StudentDto forwardGetRequest(@PathVariable (name = "memberId")Long memberId);
}
