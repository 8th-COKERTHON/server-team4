package com.team4.hackerton.domain.mission.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionType {

    TRACK_DEFAULT("트랙 기본 미션"),
    USER_CUSTOM("개인 설정 미션"),
    ;

    private final String displayName;
}
