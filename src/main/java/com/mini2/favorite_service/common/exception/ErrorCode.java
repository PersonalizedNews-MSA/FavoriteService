package com.mini2.favorite_service.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.", "500"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자입니다.","401"),
    FAVORITE_NOT_FOUND(NOT_FOUND, "좋아요를 찾을 수 없습니다.", "40402");


    private final HttpStatus status;
    private final String message;
    private final String errorCode;


    public static CustomException internalServerError() {
        return new CustomException(INTERNAL_SERVER_ERROR);
    }

    public static CustomException unauthorizedUser() {
        return new CustomException(UNAUTHORIZED_USER);
    }

    public static CustomException favoriteNotFound() {
        return new CustomException(FAVORITE_NOT_FOUND);
    }
}