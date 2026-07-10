package com.team4.hackerton.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ExampleRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    private String description;
}
