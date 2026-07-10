package com.team4.hackerton.domain.cheer.service;

import com.team4.hackerton.domain.cheer.code.CheerErrorCode;
import com.team4.hackerton.domain.cheer.dto.request.CheerRequest;
import com.team4.hackerton.domain.cheer.dto.response.CheerResponse;
import com.team4.hackerton.domain.cheer.entity.Cheer;
import com.team4.hackerton.domain.cheer.repository.CheerRepository;
import com.team4.hackerton.domain.track.entity.UserTrack;
import com.team4.hackerton.domain.track.repository.UserTrackRepository;
import com.team4.hackerton.domain.user.entity.User;
import com.team4.hackerton.global.apiPayload.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheerService {

    private final CheerRepository cheerRepository;
    private final UserTrackRepository userTrackRepository;

    @Transactional
    public void sendCheer(User sender, CheerRequest request) {
        UserTrack senderTrack = userTrackRepository.findByUserAndIsCurrentTrue(sender)
                .orElseThrow(() -> new AppException(CheerErrorCode.USER_TRACK_NOT_FOUND));

        LocalDate today = LocalDate.now();

        if (cheerRepository.existsBySenderAndTargetDate(sender, today)) {
            throw new AppException(CheerErrorCode.ALREADY_CHEERED_TODAY);
        }

        cheerRepository.save(new Cheer(sender, senderTrack.getTrack(), request.getContent(), today));
    }

    public List<CheerResponse> getRecentCheers(User user) {
        UserTrack userTrack = userTrackRepository.findByUserAndIsCurrentTrue(user)
                .orElseThrow(() -> new AppException(CheerErrorCode.USER_TRACK_NOT_FOUND));

        return cheerRepository.findTop10ByTrackOrderByCreatedAtDesc(userTrack.getTrack())
                .stream()
                .map(cheer -> new CheerResponse(
                        cheer.getSender().getId(),
                        cheer.getSender().getName(),
                        cheer.getContent(),
                        cheer.getCreatedAt()
                ))
                .toList();
    }
}
