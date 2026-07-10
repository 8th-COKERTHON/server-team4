package com.team4.hackerton.domain.auth.code;

import com.team4.hackerton.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    DUPLICATE_EMAIL(HttpStatus.CONFLICT,
            "AUTH409_1",
            "이미 가입된 이메일입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED,
            "AUTH401_1",
            "이메일 또는 비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,
            "AUTH401_2",
            "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,
            "AUTH401_3",
            "만료된 토큰입니다."),
    TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED,
            "AUTH401_4",
            "리프레시 토큰이 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,
            "AUTH404_1",
            "사용자를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
