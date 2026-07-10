package com.team4.hackerton.domain.track.code;

import com.team4.hackerton.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TrackSuccessCode implements BaseSuccessCode {

    ASSIGN_SUCCESS(HttpStatus.OK, "TRACK200_0", "트랙 배정에 성공했습니다."),
    GET_MY_TRACK_SUCCESS(HttpStatus.OK, "TRACK200_1", "내 트랙 조회에 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
