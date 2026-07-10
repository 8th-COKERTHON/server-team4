package com.team4.hackerton.domain.track.code;

import com.team4.hackerton.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TrackErrorCode implements BaseErrorCode {

    TRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "TRACK404_1", "해당 트랙을 찾을 수 없습니다."),
    USER_TRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "TRACK404_2", "소속된 트랙이 없습니다."),
    ALREADY_IN_TRACK(HttpStatus.CONFLICT, "TRACK409_1", "이미 트랙에 소속되어 있습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
