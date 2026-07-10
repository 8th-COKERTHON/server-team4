package com.team4.hackerton.domain.track.repository;

import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.track.entity.TrackType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findByTrackType(TrackType trackType);
}
