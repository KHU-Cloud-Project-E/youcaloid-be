package com.example.cloud.auth.info;

import java.util.Map;

public class KakaOAuth2User extends OAuth2UserInfo {

    private Long id;

    public KakaOAuth2User(Map<String, Object> attributes) {

        super((Map<String, Object>) attributes.get("kakao_account"));

        id = (Long) attributes.get("id");

    }

    @Override
    public String getOAuth2Id() {
        System.out.println("id: " + id.toString());
        return id.toString();
    }

    @Override
    public String getEmail() {
        System.out.println("email"+ (String) attributes.get("email"));
        return (String) attributes.get("email");
    }

    @Override
    public String getName(){
        System.out.println("nickname" + ((Map<String, Object>) attributes.get("profile")).get("nickname"));
        return (String) ((Map<String, Object>) attributes.get("profile")).get("nickname");

    }
}
