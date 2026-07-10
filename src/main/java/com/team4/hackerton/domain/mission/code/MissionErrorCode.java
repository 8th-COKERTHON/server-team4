package com.team4.hackerton.domain.mission.code;

import com.team4.hackerton.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MissionErrorCode implements BaseErrorCode {

    USER_TRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION404_1", "소속된 트랙이 없습니다."),
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION404_2", "미션을 찾을 수 없습니다."),
    ALREADY_COMPLETED(HttpStatus.CONFLICT, "MISSION409_1", "오늘 이미 완료한 미션입니다."),
    CUSTOM_MISSION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "MISSION403_1", "나를 돌보기 트랙은 개인 미션을 추가할 수 없습니다."),
    CUSTOM_MISSION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "MISSION400_1", "개인 미션은 최대 2개까지 추가할 수 있습니다."),
    CANNOT_PROCEED(HttpStatus.BAD_REQUEST, "MISSION400_2", "아직 다음 트랙으로 진행할 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
