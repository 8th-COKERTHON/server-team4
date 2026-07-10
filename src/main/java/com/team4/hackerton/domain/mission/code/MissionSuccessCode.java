package com.team4.hackerton.domain.mission.code;

import com.team4.hackerton.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MissionSuccessCode implements BaseSuccessCode {

    GET_TODAY_MISSIONS_SUCCESS(HttpStatus.OK, "MISSION200_0", "오늘의 미션 조회에 성공했습니다."),
    COMPLETE_MISSION_SUCCESS(HttpStatus.OK, "MISSION200_1", "미션 완료 처리에 성공했습니다."),
    ADD_CUSTOM_MISSION_SUCCESS(HttpStatus.OK, "MISSION200_2", "개인 미션 추가에 성공했습니다."),
    GET_PROGRESS_SUCCESS(HttpStatus.OK, "MISSION200_3", "미션 진행 현황 조회에 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
