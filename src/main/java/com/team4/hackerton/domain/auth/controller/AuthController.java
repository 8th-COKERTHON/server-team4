package com.team4.hackerton.domain.auth.controller;

import com.team4.hackerton.domain.auth.code.AuthSuccessCode;
import com.team4.hackerton.domain.auth.dto.request.LoginRequest;
import com.team4.hackerton.domain.auth.dto.request.ReissueRequest;
import com.team4.hackerton.domain.auth.dto.request.SignUpRequest;
import com.team4.hackerton.domain.auth.dto.response.SignUpResponse;
import com.team4.hackerton.domain.auth.dto.response.TokenResponse;
import com.team4.hackerton.domain.auth.service.AuthService;
import com.team4.hackerton.global.apiPayload.ApiResponse;
import com.team4.hackerton.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Operation(
            summary = "회원가입",
            description = "이메일, 비밀번호, 이름으로 신규 계정을 생성합니다.\n\n"
                    + "- 비밀번호는 영문과 숫자를 포함해 8~20자여야 합니다.\n"
                    + "- 이미 가입된 이메일이면 409 에러를 반환합니다 (AUTH409_1)."
    )
    @PostMapping("/signup")
    public ApiResponse<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return ApiResponse.onSuccess(AuthSuccessCode.SIGN_UP_SUCCESS, response);
    }

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인하고 Access/Refresh 토큰을 발급합니다.\n\n"
                    + "- 이메일 또는 비밀번호가 일치하지 않으면 401 에러를 반환합니다 (AUTH401_1).\n"
                    + "- 발급된 Refresh Token은 서버에 저장되어 이후 재발급 요청 시 대조됩니다."
    )
    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ApiResponse.onSuccess(AuthSuccessCode.LOGIN_SUCCESS, response);
    }

    @Operation(
            summary = "액세스 토큰 재발급",
            description = "Refresh Token으로 Access/Refresh 토큰을 재발급합니다.\n\n"
                    + "- 형식이 올바르지 않은 토큰이면 401 에러를 반환합니다 (AUTH401_2).\n"
                    + "- 만료된 토큰이면 401 에러를 반환합니다 (AUTH401_3).\n"
                    + "- 서버에 저장된 Refresh Token과 일치하지 않으면 401 에러를 반환합니다 (AUTH401_4)."
    )
    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissue(@RequestBody @Valid ReissueRequest request) {
        TokenResponse response = authService.reissue(request);
        return ApiResponse.onSuccess(AuthSuccessCode.REISSUE_SUCCESS, response);
    }

    @Operation(
            summary = "로그아웃",
            description = "로그인된 사용자의 Refresh Token을 무효화합니다.\n\n"
                    + "- 요청 헤더에 유효한 Access Token(`Authorization: Bearer {token}`)이 필요합니다.\n"
                    + "- 로그아웃 후에는 기존에 발급받은 Refresh Token으로 재발급을 받을 수 없습니다."
    )
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ApiResponse.onSuccess(AuthSuccessCode.LOGOUT_SUCCESS);
    }
}
