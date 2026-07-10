package com.team4.hackerton.domain.mission.service;

import com.team4.hackerton.domain.mission.code.MissionErrorCode;
import com.team4.hackerton.domain.mission.dto.request.CustomMissionRequest;
import com.team4.hackerton.domain.mission.dto.response.MissionCompleteResponse;
import com.team4.hackerton.domain.mission.dto.response.MissionItemResponse;
import com.team4.hackerton.domain.mission.dto.response.MissionListResponse;
import com.team4.hackerton.domain.mission.dto.response.MissionProgressResponse;
import com.team4.hackerton.domain.mission.entity.Mission;
import com.team4.hackerton.domain.mission.entity.MissionLog;
import com.team4.hackerton.domain.mission.repository.MissionLogRepository;
import com.team4.hackerton.domain.mission.repository.MissionRepository;
import com.team4.hackerton.domain.mission.entity.MissionType;
import com.team4.hackerton.domain.track.code.TrackErrorCode;
import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.track.entity.TrackType;
import com.team4.hackerton.domain.track.entity.UserTrack;
import com.team4.hackerton.domain.track.repository.TrackRepository;
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
    private final TrackRepository trackRepository;

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
            missionRepository.findByTrackAndTypeOrderByIdAsc(track, MissionType.TRACK_DEFAULT).forEach(mission ->
                    result.add(new MissionItemResponse(mission, completedMissionIds.contains(mission.getId())))
            );
        } else {
            List<Mission> commonMissions = missionRepository.findByTrackAndTypeOrderByIdAsc(track, MissionType.TRACK_DEFAULT);
            if (!commonMissions.isEmpty()) {
                long dayIndex = ChronoUnit.DAYS.between(userTrack.getJoinedAt(), today);
                Mission todayCommon = commonMissions.get((int) (dayIndex % commonMissions.size()));
                result.add(new MissionItemResponse(todayCommon, completedMissionIds.contains(todayCommon.getId())));
            }

            missionRepository.findTopByUserAndTrackOrderByCreatedAtDesc(user, track).ifPresent(custom ->
                    result.add(new MissionItemResponse(custom, completedMissionIds.contains(custom.getId())))
            );
        }

        return new MissionListResponse(result);
    }

    @Transactional
    public MissionCompleteResponse completeMission(User user, Long missionId) {
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

        if (!isDayCompleted(user, track, today, userTrack.getJoinedAt())) {
            return new MissionCompleteResponse(false, null);
        }

        List<LocalDate> allDates = missionLogRepository.findCompletedDatesByUserAndTrack(user, track);
        int completedDays = (int) allDates.stream()
                .filter(date -> isDayCompleted(user, track, date, userTrack.getJoinedAt()))
                .count();

        if (completedDays < track.getRequiredDays()) {
            return new MissionCompleteResponse(false, null);
        }

        userTrack.complete(today);

        TrackType nextType = track.getTrackType().next();
        if (nextType == null) {
            return new MissionCompleteResponse(true, null);
        }

        Track nextTrack = trackRepository.findByTrackType(nextType)
                .orElseThrow(() -> new AppException(TrackErrorCode.TRACK_NOT_FOUND));
        userTrackRepository.save(new UserTrack(user, nextTrack, today));

        return new MissionCompleteResponse(true, nextType.getDisplayName());
    }

    private boolean isDayCompleted(User user, Track track, LocalDate date, LocalDate joinedAt) {
        List<MissionLog> logs = missionLogRepository.findByUserAndTrackAndPerformedDate(user, track, date);
        Set<Long> completedMissionIds = logs.stream()
                .map(log -> log.getMission().getId())
                .collect(Collectors.toSet());

        List<Mission> defaultMissions = missionRepository.findByTrackAndTypeOrderByIdAsc(track, MissionType.TRACK_DEFAULT);

        if (track.getTrackType() == TrackType.SELF_CARE) {
            if (defaultMissions.isEmpty()) return false;
            Set<Long> defaultIds = defaultMissions.stream().map(Mission::getId).collect(Collectors.toSet());
            return completedMissionIds.containsAll(defaultIds);
        } else {
            if (defaultMissions.isEmpty()) return false;
            long dayIndex = ChronoUnit.DAYS.between(joinedAt, date);
            Mission todayCommon = defaultMissions.get((int) (dayIndex % defaultMissions.size()));
            if (!completedMissionIds.contains(todayCommon.getId())) return false;

            Set<Long> customMissionIds = missionRepository.findByUserAndTrackAndType(user, track, MissionType.USER_CUSTOM)
                    .stream().map(Mission::getId).collect(Collectors.toSet());
            return completedMissionIds.stream().anyMatch(customMissionIds::contains);
        }
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

    public MissionListResponse getCustomMissions(User user) {
        UserTrack userTrack = userTrackRepository.findByUserAndIsCurrentTrue(user)
                .orElseThrow(() -> new AppException(MissionErrorCode.USER_TRACK_NOT_FOUND));

        Track track = userTrack.getTrack();
        LocalDate today = LocalDate.now();

        Set<Long> completedMissionIds = missionLogRepository.findByUserAndTrackAndPerformedDate(user, track, today)
                .stream()
                .map(log -> log.getMission().getId())
                .collect(Collectors.toSet());

        List<MissionItemResponse> result = missionRepository
                .findByUserAndTrackAndType(user, track, MissionType.USER_CUSTOM)
                .stream()
                .map(mission -> new MissionItemResponse(mission, completedMissionIds.contains(mission.getId())))
                .collect(Collectors.toList());

        return new MissionListResponse(result);
    }

    @Transactional
    public MissionCompleteResponse proceedToNextTrack(User user) {
        UserTrack userTrack = userTrackRepository.findByUserAndIsCurrentTrue(user)
                .orElseThrow(() -> new AppException(MissionErrorCode.USER_TRACK_NOT_FOUND));

        Track track = userTrack.getTrack();
        LocalDate today = LocalDate.now();

        List<LocalDate> allDates = missionLogRepository.findCompletedDatesByUserAndTrack(user, track);
        int completedDays = (int) allDates.stream()
                .filter(date -> isDayCompleted(user, track, date, userTrack.getJoinedAt()))
                .count();

        if (completedDays < track.getRequiredDays()) {
            throw new AppException(MissionErrorCode.CANNOT_PROCEED);
        }

        userTrack.complete(today);

        TrackType nextType = track.getTrackType().next();
        if (nextType == null) {
            return new MissionCompleteResponse(true, null);
        }

        Track nextTrack = trackRepository.findByTrackType(nextType)
                .orElseThrow(() -> new AppException(TrackErrorCode.TRACK_NOT_FOUND));
        userTrackRepository.save(new UserTrack(user, nextTrack, today));

        return new MissionCompleteResponse(true, nextType.getDisplayName());
    }

    public MissionProgressResponse getMissionProgress(User user) {
        UserTrack userTrack = userTrackRepository.findByUserAndIsCurrentTrue(user)
                .orElseThrow(() -> new AppException(MissionErrorCode.USER_TRACK_NOT_FOUND));

        Track track = userTrack.getTrack();
        int requiredDays = track.getRequiredDays();

        List<LocalDate> allDates = missionLogRepository.findCompletedDatesByUserAndTrack(user, track);
        int completedDays = (int) allDates.stream()
                .filter(date -> isDayCompleted(user, track, date, userTrack.getJoinedAt()))
                .count();

        return new MissionProgressResponse(requiredDays, completedDays, completedDays >= requiredDays);
    }
}
