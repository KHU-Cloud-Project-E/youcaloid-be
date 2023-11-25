package com.example.cloud.web.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class ModelResponseDto {


    @Builder
    @Getter
    @AllArgsConstructor
    public static class CreateModelFinishDto{

        @Schema(description = "path")
        private String path;

        @Schema(description = "description")
        private String description;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserModelDetailDto{

        @Schema(description = "모델의 id")
        private Long model_id;

        @Schema(description = "모델의 이름")
        private String name;

        @Schema(description = "모델의 대표 이미지")
        private String image_url;

        @Schema(description = "모델에 대한 설명")
        private String description;

        @Schema(description = "모델의 공유 여부(1: 공유하지 않음, 2: 공유함)")
        private int model_check;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ListUserModelDetailDto{

        private List<UserModelDetailDto> ListUserModelDetailDto;
    }





}
