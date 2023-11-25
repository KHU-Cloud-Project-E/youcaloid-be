package com.example.cloud.converter;

import com.example.cloud.domain.mapping.Model;
import com.example.cloud.web.dto.model.ModelResponseDto;

import java.util.List;

public class ModelConverter {

    public static ModelResponseDto.UserModelDetailDto toUserModelDetailDto(Model model){

        return ModelResponseDto.UserModelDetailDto.builder()
                .model_id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .model_check(model.getShare_check())
                .image_url(model.getImage_url())
                .build();

    }

    public static ModelResponseDto.ListUserModelDetailDto toUserModelDetailListDto (List<ModelResponseDto.UserModelDetailDto> UserModelDetailList){

        return ModelResponseDto.ListUserModelDetailDto.builder()
                .ListUserModelDetailDto(UserModelDetailList)
                .build();
    }

}
