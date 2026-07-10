package com.team4.hackerton.domain.cheer.controller;

import com.team4.hackerton.domain.cheer.code.CheerSuccessCode;
import com.team4.hackerton.domain.cheer.dto.request.CheerRequest;
import com.team4.hackerton.domain.cheer.dto.response.CheerResponse;
import com.team4.hackerton.domain.cheer.service.CheerService;
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

import java.util.List;

@Tag(name = "Cheer", description = "응원 API")
@RestController
@RequestMapping("/api/cheers")
@RequiredArgsConstructor
public class CheerController {

    private final CheerService cheerService;

    @Operation(
            summary = "응원 보내기",
            description = "같은 그룹 전광판에 응원 메시지를 남깁니다. 하루 한 번만 보낼 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "응원 전송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "소속된 트랙 없음 (CHEER404_1)", content = @Content(schema = @Schema(hidden = true))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "오늘 이미 응원 보냄 (CHEER409_1)", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ApiResponse<Void> sendCheer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CheerRequest request) {
        cheerService.sendCheer(userDetails.getUser(), request);
        return ApiResponse.onSuccess(CheerSuccessCode.SEND_CHEER_SUCCESS);
    }

    @Operation(
            summary = "응원 조회",
            description = "같은 그룹 전광판에 최근 남겨진 응원 10개를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "응원 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "소속된 트랙 없음 (CHEER404_1)", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ApiResponse<List<CheerResponse>> getRecentCheers(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CheerResponse> response = cheerService.getRecentCheers(userDetails.getUser());
        return ApiResponse.onSuccess(CheerSuccessCode.GET_CHEERS_SUCCESS, response);
    }
}
