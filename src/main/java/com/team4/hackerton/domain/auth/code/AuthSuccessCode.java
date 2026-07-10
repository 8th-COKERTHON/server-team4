package com.team4.hackerton.domain.auth.code;

import com.team4.hackerton.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements BaseSuccessCode {

    SIGN_UP_SUCCESS(HttpStatus.OK,
            "AUTH200_0",
            "회원가입에 성공했습니다."),
    LOGIN_SUCCESS(HttpStatus.OK,
            "AUTH200_1",
            "로그인에 성공했습니다."),
    REISSUE_SUCCESS(HttpStatus.OK,
            "AUTH200_2",
            "토큰 재발급에 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
