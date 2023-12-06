package com.example.cloud.web.controller;


import com.example.cloud.auth.annotation.AuthUser;
import com.example.cloud.auth.provider.JwtTokenProvider;
import com.example.cloud.config.BaseResponse;
import com.example.cloud.config.Code;
import com.example.cloud.config.CustomAuthenticationException;
import com.example.cloud.converter.UserConverter;
import com.example.cloud.domain.mapping.User;
import com.example.cloud.repository.UserRepository;
import com.example.cloud.service.UserService;
import com.example.cloud.web.dto.UserRequestDto;
import com.example.cloud.web.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Tag(name = "유저")
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;
    public final UserRepository userRepository;

    public final JwtTokenProvider jwtTokenProvider;

    /*
    * 카카오 로그인 callback API
    * [GET] /oauth2
    * */
    @Hidden
    @Operation(summary = "로그인 성공시 토큰 획득 API", description = "카카오 로그인 성공시 이곳으로 자동으로 이동해 토큰 정보를 얻습니다.")
    @GetMapping("users/callback")
    public BaseResponse<UserResponseDto.TokenInfo> UserKakaoCallback(@RequestParam(name="access_token") String accessToken , @RequestParam(name = "refresh_token") String refreshToken){

        return new BaseResponse<>(UserConverter.toUserTokenInfo(accessToken,refreshToken));
    }


    /**
     * 회원 정보 수정 API
     * [PATCH] /users
     * @return BaseResponse<String>
     * */
    @Operation(summary = "회원 정보 수정 API", description = "user의 닉네임에 대해 변경하는 작업을 거친다.\n 유효한 acess token 값이 입력되어야 합니다.")
    @PatchMapping("/users")
    public BaseResponse<UserResponseDto.UserModifyDto> UserModify(@Validated @RequestBody UserRequestDto.ModifyUserDto request, @AuthUser User user){

        Long userId = user.getId();
        var user_nickname="";
        if(request.getNickname()!= null){
            user_nickname = userService.updateUsernickname(request.getNickname(),userId);
        }else{user_nickname = userRepository.findById(userId).get().getNickname();

        }



        return new BaseResponse<>(UserConverter.toModifyUserDto(user_nickname,null));


    }


    /**
     * 자동 로그인 API
     * [GET] /users/autologin
     * @return BaseResponse<String>
     * */
    @Operation(summary = "자동 로그인 API", description = "access token 이 만료되었을 경우 만료된 access token과 refresh token 을 이용해 access token 을 재발급받는 API입니다. ")
    @GetMapping("/autologin")
    public BaseResponse<UserResponseDto.TokenInfo> UserDetailsTokenInfo(@Parameter(description = "access token의 값을 Bearer ***(토큰 값) 형태로 입력해주세요 ex. Bearer eyJhbGciOiJIUzI1NiJ9~~~ ")@RequestHeader(value = "Authorization") String authorizationHeader, @Parameter(description = "만료되지 않은 Refresh-token 값을 token의 값만 입력해주세요 ")@RequestHeader(value = "Refresh-Token",required = false) String RefreshHeader, HttpServletRequest request) {
        try {
            //access token 이 만료되었으니 jwtfilter를 정상적으로 통과할 수 없을 것이다. 따라서 jwtfilter에서 제외해주어야 한다.
            //따라서 access token 에 대해 check하는 기능이 필요하다.
            //단 expired의 경우 check하지 않는다. 혹은 expired가 체크되어야만 할 수 있게 하든가 > 이건 좀 아닌 듯

            if(authorizationHeader == null){
                throw new CustomAuthenticationException("EMPTY ACCESS TOKEN");
            }

            String access_token = jwtTokenProvider.resolveToken(request);
            jwtTokenProvider.validateAccessToken(access_token);
            if (RefreshHeader == null) {
                System.out.println("here");
            }
            //refersh token 전형 vaildaToken 이 필요할 듯
            jwtTokenProvider.RefreshValidateToken(RefreshHeader);
            //refresh token 조차 만료되었을 시 자동로그인을 포기하고 다시 로그인 해야한다.

            //refresh token 이 만료되지 않았을 경우 아래 로직 실행
            //access token 복호화
            String user_email = jwtTokenProvider.getAuthentication(access_token).getName();
            System.out.println(user_email);
            String user_refresh_token = userRepository.findByEmail(user_email).get().getRefresh_token();
            System.out.println(user_refresh_token);
            System.out.println(RefreshHeader);
            if (user_refresh_token.equals(RefreshHeader)) {
                //==로 했을 때는 일치 하지 않았다.
                //access token 재발급
                return new BaseResponse<>(jwtTokenProvider.generateAccessToken(user_email));
            } else {
                System.out.println("refresh token 불일치");
                return new BaseResponse<>(Code.INVALID_REFRESH_TOKEN);
            }


        }catch (CustomAuthenticationException ee){

            System.out.println(ee.getMessage());
            return new BaseResponse<>(false, ee.getMessage(),400);
        }


    }



    // refresh token 을 받아서 본다.

    /**
     * 회원탈퇴 API
     * [POST] /users/withdraw
     * @return BaseResponse<String>
     * */

    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴를 위한 API입니다. 회원의 상태를 탈퇴중(status = 3)으로 변경하고 60일 뒤에 회원 관련 데이터를 삭제합니다\n 유효한 acess token 값이 입력되어야 합니다.")
    @PostMapping("users/withdraw")
    public BaseResponse<UserResponseDto.UserWithdrawDto> UserRemove(@AuthUser User user){

        Long userId = user.getId();
        Integer status = userService.updateUserStatus(3, userId);
        String nickname = userRepository.findById(userId).get().getNickname();
        return new BaseResponse<>(UserConverter.toWirthdrawDto(status, nickname));


    }



}
