package com.team4.hackerton.domain.cheer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheerRequest {

    @NotBlank
    @Size(max = 100)
    @Schema(description = "응원 메시지", example = "오늘도 화이팅!")
    private String content;
}
