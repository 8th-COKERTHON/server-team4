package com.team4.hackerton.domain.mission.repository;

import com.team4.hackerton.domain.mission.entity.Mission;
import com.team4.hackerton.domain.mission.entity.MissionType;
import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByTrack(Track track);

    List<Mission> findByTrackOrderByIdAsc(Track track);

    List<Mission> findByUser(User user);

    Optional<Mission> findTopByUserOrderByCreatedAtDesc(User user);

    long countByUserAndTrackAndType(User user, Track track, MissionType type);
}
