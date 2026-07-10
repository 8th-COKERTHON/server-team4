package com.team4.hackerton.domain.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MissionListResponse {

    @Schema(description = "오늘의 미션 목록")
    private final List<MissionItemResponse> missions;
}
