package com.example.cloud.auth;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String authProvider, Map<String, Object> attributes) {
        switch (authProvider) {
            case "KAKAO":
                return new KakaOAuth2User(attributes);

            default:
                throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
