package com.example.cloud.service.impl;


import com.example.cloud.repository.UserRepository;
import com.example.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;




    /*
    @Override
    public Long createUserBoardId(Long board_id, Long userId){

        userRepository.updateUserBoardId(board_id, userId);

        return userRepository.findById(userId).get().getBoardImage().getId();
    }*/

    @Override
    public String updateUsernickname(String nickname, Long userId){

        userRepository.updateUserNickname(nickname, userId);

        return userRepository.findById(userId).get().getNickname();
    };





    @Override
    public Integer updateUserStatus(int i, Long userId){

        userRepository.updateUser_Status(i, userId);
        return userRepository.findById(userId).get().getStatus();
    };



}

