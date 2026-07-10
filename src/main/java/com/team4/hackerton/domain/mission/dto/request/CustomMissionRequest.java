package com.team4.hackerton.domain.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CustomMissionRequest {

    @Schema(description = "개인 미션 제목", example = "친구에게 먼저 연락하기")
    @NotBlank(message = "미션 제목은 필수입니다.")
    private String title;
}
