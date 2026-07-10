package com.team4.hackerton.domain.cheer.code;

import com.team4.hackerton.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CheerSuccessCode implements BaseSuccessCode {

    SEND_CHEER_SUCCESS(HttpStatus.OK, "CHEER200_0", "응원 전송에 성공했습니다."),
    GET_CHEERS_SUCCESS(HttpStatus.OK, "CHEER200_1", "응원 조회에 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
