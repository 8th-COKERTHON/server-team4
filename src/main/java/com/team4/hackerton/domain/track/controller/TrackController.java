package com.team4.hackerton.domain.track.controller;

import com.team4.hackerton.domain.track.code.TrackSuccessCode;
import com.team4.hackerton.domain.track.dto.request.OnboardingRequest;
import com.team4.hackerton.domain.track.dto.response.TrackResponse;
import com.team4.hackerton.domain.track.service.TrackService;
import com.team4.hackerton.global.apiPayload.ApiResponse;
import com.team4.hackerton.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Track", description = "트랙 API")
@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @Operation(
            summary = "온보딩 트랙 배정",
            description = "온보딩 질문 선택값을 기반으로 트랙을 배정합니다.\n\n"
                    + "- `SELF_CARE` : 나를 돌보기\n"
                    + "- `GO_OUTSIDE` : 바깥으로 나가기\n"
                    + "- `CONNECT_PEOPLE` : 사람과 연결하기\n\n"
                    + "이미 트랙에 소속된 경우 409 에러를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "트랙 배정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 트랙에 소속됨 (TRACK409_1)", content = @Content(schema = @Schema(hidden = true))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "트랙을 찾을 수 없음 (TRACK404_1)", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/onboarding")
    public ApiResponse<TrackResponse> assignTrack(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid OnboardingRequest request) {
        TrackResponse response = trackService.assignTrack(userDetails.getUser(), request);
        return ApiResponse.onSuccess(TrackSuccessCode.ASSIGN_SUCCESS, response);
    }

    @Operation(
            summary = "내 트랙 조회",
            description = "현재 로그인한 유저의 소속 트랙 이름과 함께하는 멤버 수를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "트랙 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "소속된 트랙 없음 (TRACK404_2)", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/me")
    public ApiResponse<TrackResponse> getMyTrack(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TrackResponse response = trackService.getMyTrack(userDetails.getUser());
        return ApiResponse.onSuccess(TrackSuccessCode.GET_MY_TRACK_SUCCESS, response);
    }
}
