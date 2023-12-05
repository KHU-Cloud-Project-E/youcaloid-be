package com.example.cloud.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private Long accessTokenExpirationTime;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }

    @Builder //
    @Getter //
    @AllArgsConstructor //
    public static class UserLinkDto {

        private String link_info;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserModifyDto {

        private String nick_name;

        private String instagram_id;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserWithdrawDto{

        private String nick_name;

        private int status;
    }




}
