package com.team4.hackerton.domain.cheer.code;

import com.team4.hackerton.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CheerErrorCode implements BaseErrorCode {

    USER_TRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "CHEER404_1", "소속된 트랙이 없습니다."),
    ALREADY_CHEERED_TODAY(HttpStatus.CONFLICT, "CHEER409_1", "오늘 이미 응원을 보냈습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
