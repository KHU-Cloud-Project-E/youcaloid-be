package com.example.cloud.converter;

import com.example.demo.web.dto.response.UserResponseDto;

public class UserConverter {

    public static UserResponseDto.UserModifyDto toModifyUserDto(String nickname, String instagram_id){

        return UserResponseDto.UserModifyDto.builder()
                .nick_name(nickname)
                .instagram_id(instagram_id)
                .build();
    }

    public static UserResponseDto.UserLinkDto toGetUserLinkDto(String link){

        return UserResponseDto.UserLinkDto.builder()
                .link_info(link)
                .build();

    }

    public static UserResponseDto.UserWithdrawDto toWirthdrawDto(int status, String nickname){

        return UserResponseDto.UserWithdrawDto.builder()
                .status(status)
                .nick_name(nickname)
                .build();
    }

    public static UserResponseDto.TokenInfo toUserTokenInfo(String accessToken, String refreshToken){

        return UserResponseDto.TokenInfo.builder()
                .accessToken(accessToken)
                .accessTokenExpirationTime(Long.valueOf(3600000))
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(Long.valueOf(1209600000))
                .build();
    }



}
