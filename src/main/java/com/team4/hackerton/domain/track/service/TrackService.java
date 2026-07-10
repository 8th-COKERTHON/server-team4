package com.team4.hackerton.domain.track.service;

import com.team4.hackerton.domain.track.code.TrackErrorCode;
import com.team4.hackerton.domain.track.dto.request.OnboardingRequest;
import com.team4.hackerton.domain.track.dto.response.TrackResponse;
import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.track.entity.UserTrack;
import com.team4.hackerton.domain.track.repository.TrackRepository;
import com.team4.hackerton.domain.track.repository.UserTrackRepository;
import com.team4.hackerton.domain.user.entity.User;
import com.team4.hackerton.global.apiPayload.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackService {

    private final TrackRepository trackRepository;
    private final UserTrackRepository userTrackRepository;

    @Transactional
    public TrackResponse assignTrack(User user, OnboardingRequest request) {
        if (userTrackRepository.existsByUserAndIsCurrentTrue(user)) {
            throw new AppException(TrackErrorCode.ALREADY_IN_TRACK);
        }

        Track track = trackRepository.findByTrackType(request.getTrackType())
                .orElseThrow(() -> new AppException(TrackErrorCode.TRACK_NOT_FOUND));

        userTrackRepository.save(new UserTrack(user, track, LocalDate.now()));

        long memberCount = userTrackRepository.countByTrackAndIsCurrentTrue(track);
        return new TrackResponse(track.getTrackType().getDisplayName(), memberCount);
    }

    public TrackResponse getMyTrack(User user) {
        UserTrack userTrack = userTrackRepository.findByUserAndIsCurrentTrue(user)
                .orElseThrow(() -> new AppException(TrackErrorCode.USER_TRACK_NOT_FOUND));

        Track track = userTrack.getTrack();
        long memberCount = userTrackRepository.countByTrackAndIsCurrentTrue(track);
        return new TrackResponse(track.getTrackType().getDisplayName(), memberCount);
    }
}
