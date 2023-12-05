package com.example.cloud.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 관리
 */
@Getter
public enum Code {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(HttpStatus.OK, true, 2000, "요청에 성공하였습니다."),
    ERROR(HttpStatus.BAD_REQUEST, false, 4000, "server err"),

    /**
     * 400 : Request 오류, Response 오류
     */
    // Common
    REQUEST_ERROR(HttpStatus.BAD_REQUEST, false, 4000, "입력값을 확인해주세요."),
    WRONG_TYPE(HttpStatus.BAD_REQUEST, false, 4001, "잘못된 형식입니다."),
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, false, 4002, "JWT를 입력해주세요."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, false, 4003, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(HttpStatus.UNAUTHORIZED, false, 4004,"권한이 없는 유저의 접근입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, false, 4005, "만료된 JWT입니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, false, 4006, "유효하지 않는 JWT 타입입니다."),
    ILLEGAL_JWT(HttpStatus.UNAUTHORIZED,false, 4007, "JWT claims이 비어있습니다."),
    SIGNATURE_FAILE_JWT(HttpStatus.UNAUTHORIZED, false, 4008, "JWT 서명 인증에 실패했습니다."),
    WRONG_JWT(HttpStatus.UNAUTHORIZED, false, 4009,"JWT 관련 오류입니다."),
    RESPONSE_ERROR(HttpStatus.NOT_FOUND, false, 4010, "값을 불러오는데 실패하였습니다."),
    EMPTY_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,false, 4011, "Refresh Token이 입력되지 않았습니다."),
    EXCEED_MAX_FILE_SIZE(HttpStatus.BAD_REQUEST, false, 4012, "업로드할 수 있는 파일의 최대 크기를 초과하였습니다."),

    // INVALID
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,false, 4013, "사용자의 Refresh Token과 일치하지 않습니다."),
    INVALID_USER_NO(HttpStatus.BAD_REQUEST, false, 4014, "잘못된 형식의 유저 번호입니다."),
    INVALID_IDX(HttpStatus.BAD_REQUEST, false, 4015, "잘못된 형식의 인덱스입니다."),
    INVALID_PAGE_NO(HttpStatus.BAD_REQUEST, false, 4016, "잘못된 페이지 번호입니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, false, 4017, "지원하지 않는 파일 형식입니다."),

    // NOT FOUND
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, false,  4018, "포스트가 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, false,  4019, "유저가 존재하지 않습니다."),
    POST_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, false,  4020, "포스트 이미지가 존재하지 않습니다."),

    /**
     * 50 : Database, Server 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 5000, "서버 내부에서 에러가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 5001, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 5002, "서버와의 연결에 실패하였습니다."),
    IMAGE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, false, 5003, "이미지 삭제에 실패하였습니다"),


    /*
    *  6000: models 관련 오류
    * */
    EMPTY_PATH_ERROR(HttpStatus.BAD_REQUEST, false, 6000, "path를 입력하지 않았습니다. "),
    EMPTY_DESCRIPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 5001, "description을 입력하지 않았습니다.");



    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final int code;
    private final String message;

    Code(HttpStatus httpStatus, boolean isSuccess, int code, String message) {
        this.httpStatus = httpStatus;
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
