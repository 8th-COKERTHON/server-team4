package com.team4.hackerton.domain.mission.controller;

import com.team4.hackerton.domain.mission.code.MissionSuccessCode;
import com.team4.hackerton.domain.mission.dto.request.CustomMissionRequest;
import com.team4.hackerton.domain.mission.dto.response.MissionItemResponse;
import com.team4.hackerton.domain.mission.dto.response.MissionListResponse;
import com.team4.hackerton.domain.mission.dto.response.MissionProgressResponse;
import com.team4.hackerton.domain.mission.service.MissionService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mission", description = "미션 API")
@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @Operation(
            summary = "오늘의 미션 조회",
            description = "오늘 수행해야 할 미션 목록을 반환합니다.\n\n"
                    + "- `SELF_CARE` 트랙: 공통 미션 전체\n"
                    + "- 그 외 트랙: 공통 미션 1개(순환) + 개인 미션 1개"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 미션 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "소속된 트랙 없음 (MISSION404_1)", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/today")
    public ApiResponse<MissionListResponse> getTodayMissions(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        MissionListResponse response = missionService.getTodayMissions(userDetails.getUser());
        return ApiResponse.onSuccess(MissionSuccessCode.GET_TODAY_MISSIONS_SUCCESS, response);
    }

    @Operation(
            summary = "미션 완료",
            description = "특정 미션을 오늘 완료 처리합니다. 하루에 같은 미션은 1번만 완료할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "미션 완료 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "미션을 찾을 수 없음 (MISSION404_2)", content = @Content(schema = @Schema(hidden = true))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 완료한 미션 (MISSION409_1)", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/{missionId}/complete")
    public ApiResponse<Void> completeMission(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long missionId) {
        missionService.completeMission(userDetails.getUser(), missionId);
        return ApiResponse.onSuccess(MissionSuccessCode.COMPLETE_MISSION_SUCCESS);
    }

    @Operation(
            summary = "개인 미션 추가",
            description = "개인 미션을 추가합니다. `SELF_CARE` 트랙은 추가할 수 없습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "개인 미션 추가 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "나를 돌보기 트랙은 개인 미션 불가 (MISSION403_1)", content = @Content(schema = @Schema(hidden = true))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "소속된 트랙 없음 (MISSION404_1)", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/custom")
    public ApiResponse<MissionItemResponse> addCustomMission(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CustomMissionRequest request) {
        MissionItemResponse response = missionService.addCustomMission(userDetails.getUser(), request);
        return ApiResponse.onSuccess(MissionSuccessCode.ADD_CUSTOM_MISSION_SUCCESS, response);
    }

    @Operation(
            summary = "미션 진행 현황 조회",
            description = "트랙 참여 후 14일 중 미션 완료한 날 수와 다음 단계 진행 가능 여부를 반환합니다.\n\n"
                    + "- 14일 중 12일 이상 완료 시 `canProceed: true`\n"
                    + "- 하루에 미션을 1개 이상 완료하면 해당 날은 성공으로 처리"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "진행 현황 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "소속된 트랙 없음 (MISSION404_1)", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/progress")
    public ApiResponse<MissionProgressResponse> getMissionProgress(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        MissionProgressResponse response = missionService.getMissionProgress(userDetails.getUser());
        return ApiResponse.onSuccess(MissionSuccessCode.GET_PROGRESS_SUCCESS, response);
    }
}
