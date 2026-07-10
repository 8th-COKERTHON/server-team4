package com.team4.hackerton.domain.track.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrackResponse {

    @Schema(description = "트랙 이름", example = "나를 돌보기")
    private String trackName;

    @Schema(description = "현재 트랙에 소속된 멤버 수", example = "327")
    private long memberCount;
}
