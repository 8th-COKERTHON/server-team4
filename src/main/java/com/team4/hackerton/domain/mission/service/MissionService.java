package com.team4.hackerton.domain.mission.service;

import com.team4.hackerton.domain.mission.code.MissionErrorCode;
import com.team4.hackerton.domain.mission.dto.request.CustomMissionRequest;
import com.team4.hackerton.domain.mission.dto.response.MissionItemResponse;
import com.team4.hackerton.domain.mission.dto.response.MissionListResponse;
import com.team4.hackerton.domain.mission.dto.response.MissionProgressResponse;
import com.team4.hackerton.domain.mission.entity.Mission;
import com.team4.hackerton.domain.mission.entity.MissionLog;
import com.team4.hackerton.domain.mission.repository.MissionLogRepository;
import com.team4.hackerton.domain.mission.repository.MissionRepository;
import com.team4.hackerton.domain.mission.entity.MissionType;
import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.track.entity.TrackType;
import com.team4.hackerton.domain.track.entity.UserTrack;
import com.team4.hackerton.domain.track.repository.UserTrackRepository;
import com.team4.hackerton.domain.user.entity.User;
import com.team4.hackerton.global.apiPayload.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    private final MissionRepository missionRepository;
    private final MissionLogRepository missionLogRepository;
    private final UserTrackRepository userTrackRepository;

    public MissionListResponse getTodayMissions(User user) {
        UserTrack userTrack = userTrackRepository.findByUserAndIsCurrentTrue(user)
                .orElseThrow(() -> new AppException(MissionErrorCode.USER_TRACK_NOT_FOUND));

        Track track = userTrack.getTrack();
        LocalDate today = LocalDate.now();

        Set<Long> completedMissionIds = missionLogRepository.findByUserAndTrackAndPerformedDate(user, track, today)
                .stream()
                .map(log -> log.getMission().getId())
                .collect(Collectors.toSet());

        List<MissionItemResponse> result = new ArrayList<>();

        if (track.getTrackType() == TrackType.SELF_CARE) {
            missionRepository.findByTrackOrderByIdAsc(track).forEach(mission ->
                    result.add(new MissionItemResponse(mission, completedMissionIds.contains(mission.getId())))
            );
        } else {
            List<Mission> commonMissions = missionRepository.findByTrackOrderByIdAsc(track);
            if (!commonMissions.isEmpty()) {
                long dayIndex = ChronoUnit.DAYS.between(userTrack.getJoinedAt(), today);
                Mission todayCommon = commonMissions.get((int) (dayIndex % commonMissions.size()));
                result.add(new MissionItemResponse(todayCommon, completedMissionIds.contains(todayCommon.getId())));
            }

            missionRepository.findTopByUserOrderByCreatedAtDesc(user).ifPresent(custom ->
                    result.add(new MissionItemResponse(custom, completedMissionIds.contains(custom.getId())))
            );
        }

        return new MissionListResponse(result);
    }

    @Transactional
    public void completeMission(User user, Long missionId) {
        UserTrack userTrack = userTrackRepository.findByUserAndIsCurrentTrue(user)
                .orElseThrow(() -> new AppException(MissionErrorCode.USER_TRACK_NOT_FOUND));

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new AppException(MissionErrorCode.MISSION_NOT_FOUND));

        Track track = userTrack.getTrack();
        LocalDate today = LocalDate.now();

        if (missionLogRepository.existsByUserAndMissionAndTrackAndPerformedDate(user, mission, track, today)) {
            throw new AppException(MissionErrorCode.ALREADY_COMPLETED);
        }

        missionLogRepository.save(new MissionLog(user, mission, track, today));
    }

    @Transactional
    public MissionItemResponse addCustomMission(User user, CustomMissionRequest request) {
        UserTrack userTrack = userTrackRepository.findByUserAndIsCurrentTrue(user)
                .orElseThrow(() -> new AppException(MissionErrorCode.USER_TRACK_NOT_FOUND));

        Track track = userTrack.getTrack();

        if (track.getTrackType() == TrackType.SELF_CARE) {
            throw new AppException(MissionErrorCode.CUSTOM_MISSION_NOT_ALLOWED);
        }

        long customCount = missionRepository.countByUserAndTrackAndType(user, track, MissionType.USER_CUSTOM);
        if (customCount >= 2) {
            throw new AppException(MissionErrorCode.CUSTOM_MISSION_LIMIT_EXCEEDED);
        }

        Mission mission = missionRepository.save(Mission.ofUserCustom(user, track, request.getTitle()));
        return new MissionItemResponse(mission, false);
    }

    public MissionProgressResponse getMissionProgress(User user) {
        UserTrack userTrack = userTrackRepository.findByUserAndIsCurrentTrue(user)
                .orElseThrow(() -> new AppException(MissionErrorCode.USER_TRACK_NOT_FOUND));

        Track track = userTrack.getTrack();
        int requiredDays = track.getRequiredDays();

        int completedDays = missionLogRepository.findCompletedDatesByUserAndTrack(user, track).size();

        return new MissionProgressResponse(requiredDays, completedDays, completedDays >= requiredDays);
    }
}
