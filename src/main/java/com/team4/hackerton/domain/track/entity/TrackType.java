package com.team4.hackerton.domain.track.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrackType {

    SELF_CARE("나를 돌보기"),
    GO_OUTSIDE("바깥으로 나가기"),
    CONNECT_PEOPLE("사람과 연결하기"),
    ;

    private final String displayName;

    public TrackType next() {
        return switch (this) {
            case SELF_CARE -> GO_OUTSIDE;
            case GO_OUTSIDE -> CONNECT_PEOPLE;
            case CONNECT_PEOPLE -> null;
        };
    }
}
