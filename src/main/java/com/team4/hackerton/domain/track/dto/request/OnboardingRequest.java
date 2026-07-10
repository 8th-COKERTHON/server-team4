package com.team4.hackerton.domain.track.dto.request;

import com.team4.hackerton.domain.track.entity.TrackType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OnboardingRequest {

    @Schema(description = "선택한 트랙 타입", example = "SELF_CARE",
            allowableValues = {"SELF_CARE", "GO_OUTSIDE", "CONNECT_PEOPLE"})
    @NotNull(message = "트랙 타입은 필수입니다.")
    private TrackType trackType;
}
