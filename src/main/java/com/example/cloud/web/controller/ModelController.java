package com.example.cloud.web.controller;

import com.example.cloud.auth.annotation.AuthUser;
import com.example.cloud.config.BaseResponse;
import com.example.cloud.domain.mapping.Model;
import com.example.cloud.domain.mapping.User;
import com.example.cloud.service.ModelService;
import com.example.cloud.web.dto.model.ModelRequestDto;
import com.example.cloud.web.dto.model.ModelResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.cloud.config.Code.*;

@Slf4j
@Tag(name = "모델 관련 기능")
@RequestMapping("/")
@RestController //Controller : & ResController:
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    /* 1
     * 모델 업로드 API
     * [POST] /models
     * parameter: String path, String model description
     * Finish: OK
     * */

    @Operation(summary = "모델 업로드 기능", description = "사용자의 모델 path, model description을 전달")
    @PostMapping("/models")
    public BaseResponse<ModelResponseDto.CreateModelFinishDto> CreateModelController(@RequestBody(required = false) ModelRequestDto.CreateModelDto request, @AuthUser User user){

        System.out.println("path: " + request.getPath());
        System.out.println("name" + request.getName());
        System.out.println("user id: " + user.getId());

        if(request.getPath() == null){
            return new BaseResponse<>(EMPTY_PATH_ERROR);
        }
        if(request.getName() == null){
            //return new BaseResponse<>(EMPTY_DESCRIPTION_ERROR);
        }

        Model model = modelService.createUserModel(request.getPath(),request.getName(), user);

        
        if(model == null){
            System.out.println("model이 없는뎅 생성 안됐나봄 ㅋ");
        }
        return new BaseResponse<>(SUCCESS);
    }

    /* 2
     * 나의 모델 리스트 확인 API
     * [GET] /oauth2
     * parameters: user_id
     * finish: OK
     * */
    @Operation(summary = "나의 모델 리스트 확인 기능", description = "사용자의 모델 정보를 list로 확인할 수 있습니다. ")
    @GetMapping("/users/models")
    public BaseResponse<ModelResponseDto.ListUserModelDetailDto> FindUserModelListController(@AuthUser User user){

        return new BaseResponse<>(modelService.findUserModelList(user.getId()));
    }



    /* 3
     * 공유 모델 리스트 확인 기능
     * [GET] /oauth2
     * parameters: last_check_model_id
     * finish: ok
     * */
    @Operation(summary = "모델 리스트 확인 기능", description = "모델 리스트를 확인할 수 있다. + 검색 기능 포함")
    @GetMapping("/models")
    public BaseResponse<ModelResponseDto.ListUserModelDetailDto> FindModelListController(@Validated @RequestBody(required = false) ModelRequestDto.FindModelListDto request){

        if(request.getLast_id()==null){
            return new BaseResponse<>(ERROR);
        }

        return new BaseResponse<>(modelService.findModelList(request.getLast_id(),request.getName()));
    }


    /* 4
     * 특정 모델 상세 정보 확인하기
     * [GET] /oauth2
     * */
    @Operation(summary = "모델 상세 정보 확인 기능", description = "모델 리스트를 확인할 수 있다. + 검색 기능 포함")
    @GetMapping("/models/id")
    public BaseResponse<ModelResponseDto.UserModelDetailDto> FindUserModelController(@Validated @RequestBody(required = false) ModelRequestDto.FindUserModelDto request, @AuthUser User user){

        return new BaseResponse<>(modelService.findModelDetail(request.getModel_id()));
    }

    /* 5
     * 특정 모델 상세 정보 확인하기
     * [GET] /oauth2
     * */
    @Operation(summary = "모델 상세 정보 확인 기능", description = "모델 리스트를 확인할 수 있다. + 검색 기능 포함")
    @GetMapping("/models/idxs")
    public BaseResponse<ModelResponseDto.UserModelDetailDto> FindModelController(@Validated @RequestBody(required = false) ModelRequestDto.FindUserModelDto request){

        return new BaseResponse<>(modelService.findModelDetail(request.getModel_id()));
    }

    /* 5
     * 모델 즐겨찾기 추가하기
     * [GET] /oauth2
     * 일단 안 해도 됨
     * */

    @Operation(summary = "모델 즐겨찾기 설정", description = "특정 모델을 즐겨찾기 설정 할 수 있다, 로그인 유저만 사용가능하다")
    @PostMapping("/users/stars")
    public BaseResponse CreateStarModelController(@Validated @RequestBody(required = false) ModelRequestDto.FindUserModelDto request, @AuthUser User user){

        return null;
    }



    /* 6
     * 모델 즐겨찾기 삭제하기
     * [GET] /oauth2
     * 일단 안 해도 됨
     * */
    @Operation(summary = "모델 즐겨찾기를 해제", description = "특정 모델을 즐겨찾기 해제할 수 있다, 로그인 유저만 사용가능하다. ")
    @DeleteMapping("/users/stars")
    public BaseResponse DeleteStarModelController(@Validated @RequestBody(required = false) ModelRequestDto.FindUserModelDto request, @AuthUser User user){

        return null;
    }

    /* 7
     * 모델 수정 api
     * [GET] /oauth2
     * finish: ok
     * */
    @Operation(summary = "사용자의 모델을 수정", description = "특정 모델의 정보를 수정한다.")
    @PatchMapping("/users")
    public BaseResponse UpdateUserModelController(@Validated @RequestBody(required = false) ModelRequestDto.UpdateUserModelDto request, @AuthUser User user){

        System.out.println("name" + request.getName());
        return new BaseResponse<>(modelService.updateUserModel(request.getModel_id(),request.getName(),request.getDescription(),request.getShare_check()));

    }


    /* 8
     * 모델 사진 등록 api
     * [GET] /oauth2
     * */
    @Operation(summary = "모델 즐겨찾기를 해제", description = "특정 모델을 즐겨찾기 해제할 수 있다, 로그인 유저만 사용가능하다. ")
    @PatchMapping(value = "/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse UpdateUserModelImageController(@RequestParam("file") MultipartFile file, @RequestParam Long modelId,@AuthUser User user){

        String s3Url = modelService.updateUserModelImage(file,modelId);
        return new BaseResponse<>(s3Url);
    }

    /* 9
     * 모델 id 재발급 api
     * [GET] /oauth2
     * */

    @Operation(summary = "모델 id 재발급", description = "특정 모델을 id값만 변경할 수 있다.")
    @PutMapping("/models/re")
    public BaseResponse UpdateUserModelIdController(@Validated @RequestBody(required = false) ModelRequestDto.FindUserModelDto request, @AuthUser User user){

        System.out.println(request.getModel_id());
        return new BaseResponse(modelService.updateUserModelId(request.getModel_id()));
    }






}
