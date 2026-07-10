package com.team4.hackerton.domain.mission.repository;

import com.team4.hackerton.domain.mission.entity.Mission;
import com.team4.hackerton.domain.mission.entity.MissionLog;
import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface MissionLogRepository extends JpaRepository<MissionLog, Long> {

    List<MissionLog> findByUserAndTrackAndPerformedDate(User user, Track track, LocalDate performedDate);

    boolean existsByUserAndMissionAndTrackAndPerformedDate(User user, Mission mission, Track track, LocalDate performedDate);

    @Query("SELECT DISTINCT ml.performedDate FROM MissionLog ml WHERE ml.user = :user AND ml.track = :track")
    List<LocalDate> findCompletedDatesByUserAndTrack(@Param("user") User user, @Param("track") Track track);
}
