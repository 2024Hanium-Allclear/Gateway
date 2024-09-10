package com.allcleargateway.gateway.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenDto {

    @Getter
    @Builder
    public static class GetAccessTokenResponseDto{
        private Long id;

        String accessToken;
    }

    @Getter
    @Builder
    public static class UpdateRefreshTokenRequestDto{
        private Long id;

        String refreshToken;
    }
}
