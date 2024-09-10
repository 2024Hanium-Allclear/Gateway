package com.allcleargateway.gateway.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class LoginDto {
    @Builder
    @Getter
    public static class RequestDto{
        private String loginId;
        private String password;
    }

    @Getter
    @Builder
    public static class ResponseDto{
        String refreshToken;
        String accessToken;
    }
}
