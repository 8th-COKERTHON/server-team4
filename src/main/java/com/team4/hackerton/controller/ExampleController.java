package com.team4.hackerton.controller;

import com.team4.hackerton.dto.request.ExampleRequest;
import com.team4.hackerton.dto.response.ExampleResponse;
import com.team4.hackerton.global.apiPayload.ApiResponse;
import com.team4.hackerton.global.apiPayload.code.GeneralErrorCode;
import com.team4.hackerton.global.apiPayload.code.GeneralSuccessCode;
import com.team4.hackerton.global.apiPayload.exception.AppException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    // 결과 있는 성공 응답
    @GetMapping("/{id}")
    public ApiResponse<ExampleResponse> getExample(@PathVariable Long id) {
        if (id <= 0) {
            throw new AppException(GeneralErrorCode.NOT_FOUND);
        }
        ExampleResponse response = new ExampleResponse(id, "예시 이름", "예시 설명");
        return ApiResponse.onSuccess(GeneralSuccessCode.FOUND, response);
    }

    // 결과 없는 성공 응답
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteExample(@PathVariable Long id) {
        // 삭제 로직
        return ApiResponse.onSuccess(GeneralSuccessCode.OK);
    }

    // 요청 바디 받는 성공 응답
    @PostMapping
    public ApiResponse<ExampleResponse> createExample(@RequestBody @Valid ExampleRequest request) {
        ExampleResponse response = new ExampleResponse(1L, request.getName(), request.getDescription());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, response);
    }
}
