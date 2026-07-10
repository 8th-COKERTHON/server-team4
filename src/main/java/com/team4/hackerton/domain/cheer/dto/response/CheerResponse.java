package com.team4.hackerton.domain.cheer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CheerResponse {

    @Schema(description = "보낸 사람 ID", example = "10")
    private final Long senderId;

    @Schema(description = "보낸 사람 이름", example = "이OO")
    private final String senderName;

    @Schema(description = "응원 메시지", example = "오늘도 화이팅!")
    private final String content;

    @Schema(description = "응원 보낸 시각")
    private final LocalDateTime createdAt;
}
