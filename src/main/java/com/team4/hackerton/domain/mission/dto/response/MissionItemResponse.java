package com.team4.hackerton.domain.mission.dto.response;

import com.team4.hackerton.domain.mission.entity.Mission;
import com.team4.hackerton.domain.mission.entity.MissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MissionItemResponse {

    @Schema(description = "미션 ID", example = "1")
    private final Long missionId;

    @Schema(description = "미션 제목", example = "오늘 10분 스트레칭하기")
    private final String title;

    @Schema(description = "미션 타입", example = "TRACK_DEFAULT")
    private final MissionType type;

    @Schema(description = "오늘 완료 여부", example = "false")
    private final boolean isDone;

    public MissionItemResponse(Mission mission, boolean isDone) {
        this.missionId = mission.getId();
        this.title = mission.getTitle();
        this.type = mission.getType();
        this.isDone = isDone;
    }
}
