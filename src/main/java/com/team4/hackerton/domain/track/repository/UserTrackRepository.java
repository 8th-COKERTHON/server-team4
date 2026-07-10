package com.team4.hackerton.domain.track.repository;

import com.team4.hackerton.domain.track.entity.Track;
import com.team4.hackerton.domain.track.entity.UserTrack;
import com.team4.hackerton.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTrackRepository extends JpaRepository<UserTrack, Long> {

    Optional<UserTrack> findByUserAndIsCurrentTrue(User user);

    List<UserTrack> findByTrackAndIsCurrentTrue(Track track);

    long countByTrackAndIsCurrentTrue(Track track);

    boolean existsByUserAndIsCurrentTrue(User user);
}
