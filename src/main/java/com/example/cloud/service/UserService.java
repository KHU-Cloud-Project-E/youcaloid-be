package com.example.cloud.service;

public interface UserService {

    String updateUsernickname(String nickname, Long userId);

    Integer updateUserStatus(int i, Long userId);
}
