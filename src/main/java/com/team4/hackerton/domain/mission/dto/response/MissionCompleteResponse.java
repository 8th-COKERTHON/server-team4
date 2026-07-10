package com.team4.hackerton.domain.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissionCompleteResponse {

    @Schema(description = "트랙 완료 및 다음 트랙 전환 여부", example = "true")
    private final boolean trackCompleted;

    @Schema(description = "전환된 다음 트랙 이름 (마지막 트랙 완료 시 null)", example = "바깥으로 나가기", nullable = true)
    private final String nextTrackName;
}
