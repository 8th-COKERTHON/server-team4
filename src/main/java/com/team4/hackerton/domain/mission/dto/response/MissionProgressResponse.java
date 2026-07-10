package com.team4.hackerton.domain.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissionProgressResponse {

    @Schema(description = "다음 레벨로 가기 위한 미션 수행 일수", example = "14")
    private final int requiredDays;

    @Schema(description = "현재 트랙에서 미션을 1개 이상 완료한 날 수", example = "8")
    private final int completedDays;

    @Schema(description = "다음 레벨 진행 가능 여부", example = "false")
    private final boolean canProceed;
}
