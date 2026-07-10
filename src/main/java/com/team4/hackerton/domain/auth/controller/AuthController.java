package com.team4.hackerton.domain.auth.controller;

import com.team4.hackerton.domain.auth.code.AuthSuccessCode;
import com.team4.hackerton.domain.auth.dto.request.LoginRequest;
import com.team4.hackerton.domain.auth.dto.request.ReissueRequest;
import com.team4.hackerton.domain.auth.dto.request.SignUpRequest;
import com.team4.hackerton.domain.auth.dto.response.SignUpResponse;
import com.team4.hackerton.domain.auth.dto.response.TokenResponse;
import com.team4.hackerton.domain.auth.service.AuthService;
import com.team4.hackerton.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "회원가입 및 로그인 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponse<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return ApiResponse.onSuccess(AuthSuccessCode.SIGN_UP_SUCCESS, response);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ApiResponse.onSuccess(AuthSuccessCode.LOGIN_SUCCESS, response);
    }

    @Operation(summary = "액세스 토큰 재발급")
    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissue(@RequestBody @Valid ReissueRequest request) {
        TokenResponse response = authService.reissue(request);
        return ApiResponse.onSuccess(AuthSuccessCode.REISSUE_SUCCESS, response);
    }
}
