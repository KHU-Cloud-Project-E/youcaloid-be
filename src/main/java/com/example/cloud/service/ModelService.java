package com.example.cloud.service;

import com.example.cloud.domain.mapping.Model;
import com.example.cloud.domain.mapping.User;
import com.example.cloud.web.dto.model.ModelResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ModelService {

    Model createUserModel(String path, String name, User user);

    ModelResponseDto.ListUserModelDetailDto findUserModelList(Long user_id);

    ModelResponseDto.ListUserModelDetailDto findModelList(Long modelId,String name);

    ModelResponseDto.UserModelDetailDto findModelDetail(Long modelId);

    Boolean updateUserModel(Long modelId, String name, String description, int share);

    String updateUserModelImage(MultipartFile file, Long modelId);

    Boolean updateUserModelId(Long modelId);

    String DeleteUserModel(Long modelId);
}
