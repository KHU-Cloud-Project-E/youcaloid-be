package com.example.cloud.web.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class ModelRequestDto {

    @Schema(description = "User 생성 회원가입 Dto")
    @Getter
    @Setter
    public static class CreateModelDto{

        @Schema(description = "path")
        @NotEmpty(message = "path가 전송되어야 합니다.")
        private String path;

        @Schema(description = "description")
        @NotEmpty(message = "description를 입력해주세요")
        private String name;

    }

    @Schema(description = "전체 모델 리스트 확인 Request Dto")
    @Getter
    @Setter
    public static class FindModelListDto{

        @Schema(description = "마지막으로 확인한 id값을 보여줍니다.")
        private Long lastId;

        private String name;

    }

    @Schema(description = "한 유저의 모델 리스트 확인 Request Dto")
    @Getter
    @Setter
    public static class FindUserModelListDto{

        @Schema(description = "사용자의 user_id")
        private Long user_id;

    }

    @Schema(description = "한 유저의 모델 상세 정보 확인 Request Dto")
    @Getter
    @Setter
    public static class FindUserModelDto{

        @Schema(description = "사용자의 model_id")
        private Long modelId;

    }

    @Schema(description = "모델 정보 수정 Request Dto")
    @Getter
    @Setter
    public static class UpdateUserModelDto{

        @Schema(description = "사용자의 model_id")
        private Long model_id;

        @Schema(description = "model의 name")
        private String name;

        @Schema(description = "모델에 대한 설명")
        private String description;

        @Schema(description = "공유 여부(1: 공유, 0: 공유 안함)")
        private int share_check;

    }

    @Schema(description = "한 유저의 모델 상세 정보 확인 Request Dto")
    @Getter
    @Setter
    public static class UpdateUserModelImageDto{

        @Schema(description = "사용자의 model_id")
        private Long model_id;

        private String image_url;


    }
}
